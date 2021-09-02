package me.shahim.booksy.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class AccountRepository (c: Application) {
    private var mContext: Application = c
    val loggedIn: LiveData<Boolean> = _loggedIn
    init {

    }
    companion object {
        private val _loggedIn: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    }

    fun getUserID(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun refreshLoginStatus() {

    }
}