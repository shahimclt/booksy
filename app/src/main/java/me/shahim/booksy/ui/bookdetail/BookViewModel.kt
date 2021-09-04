package me.shahim.booksy.ui.bookdetail

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import me.shahim.booksy.data.model.Book
import me.shahim.booksy.data.repository.BookRepository

class BookViewModel(application: Application) : AndroidViewModel(application) {

    private val bookRepo: BookRepository = BookRepository()

    private val _bookId = MutableLiveData("")

    fun setBookId(id: String) {
        _bookId.value = id
    }

    val book: LiveData<Book> = Transformations.switchMap(_bookId) { id ->

        val ld: MutableLiveData<Book?> = MutableLiveData(null)
        bookRepo.getBookRef(id).addSnapshotListener(EventListener<DocumentSnapshot> { value, e ->
            if (e != null) {
                //TODO notify error
            }
            val book = value?.toObject(Book::class.java)
            ld.postValue(book)
        })

        return@switchMap ld
    }
}