package me.shahim.booksy.ui.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autosmarts.gondor.data.repository.AccountRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class AccountViewModel(application: Application) : AndroidViewModel(application) {

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading
    val user = FirebaseAuth.getInstance().currentUser

    //login status
    private val _loggedIn = MutableLiveData<Boolean>()
    val loggedIn: LiveData<Boolean> = _loggedIn
    init {
        _loggedIn.value = user!=null
    }
}