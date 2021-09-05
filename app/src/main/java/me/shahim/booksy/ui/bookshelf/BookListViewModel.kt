package me.shahim.booksy.ui.bookshelf

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import me.shahim.booksy.data.model.Book
import me.shahim.booksy.data.model.UserProfile
import me.shahim.booksy.data.repository.AccountRepository
import me.shahim.booksy.data.repository.BookRepository
import me.xdrop.fuzzywuzzy.FuzzySearch
import java.time.LocalTime

class BookListViewModel(application: Application) : AndroidViewModel(application) {

    private val bookRepo: BookRepository = BookRepository()
    private val accountRepo: AccountRepository = AccountRepository()

    private val _greeting = MutableLiveData("")
    val greeting: LiveData<String> = _greeting

    private val _userName = MutableLiveData("")
    val userName: LiveData<String> = _userName

    private val _allBooks = MutableLiveData<List<Book>>(listOf())
    val allBooks: LiveData<List<Book>> = _allBooks

    private val _homeFilter = MutableLiveData<String>("")
    val homeFilter: LiveData<String> = _homeFilter

    private val _filteredBooks: MediatorLiveData<List<Book>> by lazy {
        val med = MediatorLiveData<List <Book>>()
        med.addSource(_allBooks) { filterBookList() }
        med.addSource(_homeFilter) { filterBookList() }
        med
    }
    val filteredBooks: LiveData<List<Book>> = _filteredBooks

    private val _ownedBookIds = MutableLiveData<List<String>>(listOf())

    private val _ownedBooks: MediatorLiveData<List<Book>> by lazy {
        val med = MediatorLiveData<List <Book>>()
        med.addSource(_allBooks) { findOwnedBooks() }
        med.addSource(_ownedBookIds) { findOwnedBooks() }
        getUserBookIds()
        med
    }
    val ownedBooks: LiveData<List<Book>> = _ownedBooks

    init {
        setGreeting()
        setInfo()
        getAllBooks()
    }

    companion object {
        const val MORNING: String = "M"
        const val AFTERNOON: String = "A"
        const val EVENING: String = "E"

        private val MORNING_END = LocalTime.of(11,55)
        private val NOON_END = LocalTime.of(15,0)
        private val NIGHT_END = LocalTime.of(3,55)
    }

    private fun filterBookList() {
        val bookList = _allBooks.value?.toMutableList()?: mutableListOf()
        val filter = _homeFilter.value?:""

        _filteredBooks.value = bookList.filter {
            return@filter FuzzySearch.partialRatio(filter,"${it.title} ${it.author}") > 60
        }
    }

    private fun findOwnedBooks() {
        val bookList = _allBooks.value?.toMutableList()?: mutableListOf()
        val ids = _ownedBookIds.value?: listOf()

        _ownedBooks.value = bookList.filter {
            return@filter ids.contains(it.id)
        }
    }

    private fun setGreeting() {
        val time = LocalTime.now()
        _greeting.value = when {
            time.isBefore(NIGHT_END) -> EVENING
            time.isBefore(MORNING_END) -> MORNING
            time.isBefore(NOON_END) -> AFTERNOON
            else -> EVENING
        }
    }

    private fun setInfo() {
        val user = AccountRepository().getUser()
        user?.apply {
            _userName.value = displayName
        }
    }

    private fun getAllBooks() {
        bookRepo.getBookListRef().addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
            if (e != null) {
                //TODO notify error
            }
            val bookList : MutableList<Book> = mutableListOf()
            for (doc in value!!) {
                val book = doc.toObject(Book::class.java)
                bookList.add(book)
            }
            _allBooks.postValue(bookList)
        })
    }

    private fun getUserBookIds() {
        accountRepo.getUserProfile()
            .addSnapshotListener(EventListener<DocumentSnapshot> { value, e ->
                if (e != null) {
                    //TODO notify error
                }
                val profile = value?.toObject(UserProfile::class.java)
                profile?.let {
                    _ownedBookIds.postValue(profile.ownedBooks)
                }
            })
    }
}