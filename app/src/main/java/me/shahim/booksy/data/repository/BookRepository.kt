package me.shahim.booksy.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
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

    fun getBookRef(id: String): DocumentReference {
        return db.collection("books").document(id)
    }

    fun addBookToUser(bookId: String, userId: String): Task<Void> {
        return db.collection("users").document(userId)
            .update("ownedBooks", FieldValue.arrayUnion(bookId))
    }
}

