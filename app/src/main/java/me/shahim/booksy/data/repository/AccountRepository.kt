package me.shahim.booksy.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AccountRepository () {
    val loggedIn: LiveData<Boolean> = _loggedIn
    init {

    }
    companion object {
        private val _loggedIn: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    }

    fun getUserID(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun getUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun refreshLoginStatus() {

    }
}