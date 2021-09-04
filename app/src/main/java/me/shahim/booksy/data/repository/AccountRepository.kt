package me.shahim.booksy.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AccountRepository () {
    private val db = Firebase.firestore

    fun storeUserToDB() {
        getUser()?.let { user ->
            db.collection("users").document(user.uid)
                .set(
                    hashMapOf(
                        "name" to user.displayName,
                        "email" to user.email,
                        "photo" to user.photoUrl.toString(),
                        "ownedBooks" to arrayListOf<String>(),
                    ), SetOptions.merge()
                )
        }
    }

    fun getUserProfile(): DocumentReference {
        return db.collection("users").document(getUserID()?:"")
    }

    fun getUserID(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    fun getUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun refreshLoginStatus() {

    }
}