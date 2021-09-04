package me.shahim.booksy.ui.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import me.shahim.booksy.data.repository.AccountRepository
import me.shahim.booksy.data.repository.BookRepository

class AccountViewModel(application: Application) : AndroidViewModel(application) {

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val accountRepo: AccountRepository = AccountRepository()

    val user = accountRepo.getUser()

    //login status
    private val _loggedIn = MutableLiveData<Boolean>()
    val loggedIn: LiveData<Boolean> = _loggedIn
    init {
        _loggedIn.value = user!=null
    }

    fun loggedIn() {
        accountRepo.storeUserToDB()
    }
}