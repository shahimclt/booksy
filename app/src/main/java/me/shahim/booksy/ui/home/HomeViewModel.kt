package me.shahim.booksy.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import me.shahim.booksy.data.model.Book
import me.shahim.booksy.data.repository.BookRepository

class HomeViewModel : ViewModel() {

    private val bookRepo: BookRepository = BookRepository()

    private val _allBooks = MutableLiveData<List<Book>>()
    val allBooks: LiveData<List<Book>> = _allBooks

    init {
        getAllBooks()
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