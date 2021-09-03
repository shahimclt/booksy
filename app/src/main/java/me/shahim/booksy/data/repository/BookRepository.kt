package me.shahim.booksy.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import me.shahim.booksy.data.model.Book

class BookRepository {

//    private var mContext: Application = c

    private val db = Firebase.firestore
//    private val owner = FirebaseAuth.getInstance().currentUser?.uid

    companion object {
        var books: List<Book>? = null
//        var myBooks
    }

    fun getBookListRef(): CollectionReference {
        return db.collection("books")
    }
}

