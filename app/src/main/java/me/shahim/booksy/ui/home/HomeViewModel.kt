package me.shahim.booksy.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import me.shahim.booksy.data.model.Book
import me.shahim.booksy.data.repository.AccountRepository
import me.shahim.booksy.data.repository.BookRepository
import java.time.LocalTime

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val bookRepo: BookRepository = BookRepository()

    private val _greeting = MutableLiveData("")
    val greeting: LiveData<String> = _greeting

    private val _userName = MutableLiveData("")
    val userName: LiveData<String> = _userName

    private val _allBooks = MutableLiveData<List<Book>>()
    val allBooks: LiveData<List<Book>> = _allBooks

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
}