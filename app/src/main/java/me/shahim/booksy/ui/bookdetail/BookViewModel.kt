package me.shahim.booksy.ui.bookdetail

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import me.shahim.booksy.data.model.Book
import me.shahim.booksy.data.model.UserProfile
import me.shahim.booksy.data.repository.AccountRepository
import me.shahim.booksy.data.repository.BookRepository

class BookViewModel(application: Application) : AndroidViewModel(application) {

    private val bookRepo: BookRepository = BookRepository()
    private val accountRepo: AccountRepository = AccountRepository()

    private val _bookId = MutableLiveData("")

    fun setBookId(id: String) {
        _bookId.value = id
        loadBook(id)
    }

    private var bookRefListener: ListenerRegistration? = null

    private val _book: MutableLiveData<Book?> = MutableLiveData(null)
    val book: LiveData<Book?> = _book

    private val _bookOwned = MutableLiveData<Boolean>(false)
    val bookOwned: LiveData<Boolean> = _bookOwned

    private fun loadBook(id: String) {
        bookRefListener?.remove()
        bookRepo.getBookRef(id).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val book = document.toObject(Book::class.java)
                    _book.postValue(book)
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

        accountRepo.getUserProfile()
            .addSnapshotListener(EventListener<DocumentSnapshot> { value, e ->
                if (e != null) {
                    //TODO notify error
                }
                val profile = value?.toObject(UserProfile::class.java)
                profile?.let {
                    _bookOwned.postValue(it.doesOwnBook(id))
                }
            })
    }

    private val _bookBuyLoading = MutableLiveData<Boolean>(false)
    val bookBuyLoading: LiveData<Boolean> = _bookBuyLoading

    fun buy() {
        val bookId = _bookId.value?:return
        val userId = accountRepo.getUserID()?:return

        _bookBuyLoading.postValue(true)

        bookRepo.addBookToUser(bookId,userId)
            .addOnSuccessListener {
                _bookBuyLoading.postValue(false)
            }
            .addOnFailureListener { e ->
                _bookBuyLoading.postValue(false)
            }
    }

    override fun onCleared() {
        super.onCleared()
        bookRefListener?.remove()
    }
}