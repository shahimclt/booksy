package me.shahim.booksy.ui.bookdetail

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import me.shahim.booksy.data.model.Book
import me.shahim.booksy.data.repository.BookRepository

class BookViewModel(application: Application) : AndroidViewModel(application) {

    private val bookRepo: BookRepository = BookRepository()

    private val _bookId = MutableLiveData("")

    fun setBookId(id: String) {
        _bookId.value = id
        loadBook(id)
    }

    private var bookRefListener: ListenerRegistration? = null

    private val _book: MutableLiveData<Book?> = MutableLiveData(null)
    val book: LiveData<Book?> = _book

    private fun loadBook(id: String) {
        bookRefListener?.remove()
        bookRefListener = bookRepo.getBookRef(id).addSnapshotListener(EventListener<DocumentSnapshot> { value, e ->
            if (e != null) {
                //TODO notify error
            }
            val book = value?.toObject(Book::class.java)
            _book.postValue(book)
        })
    }

    override fun onCleared() {
        super.onCleared()
        bookRefListener?.remove()
    }
}