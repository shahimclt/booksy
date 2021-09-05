package me.shahim.booksy.ui.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import me.shahim.booksy.data.repository.AccountRepository
import me.shahim.booksy.data.repository.BookRepository

class AccountViewModel(application: Application) : AndroidViewModel(application) {

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val accountRepo: AccountRepository = AccountRepository()

    private val _user: MutableLiveData<FirebaseUser?> = MutableLiveData(accountRepo.getUser())
    val user: LiveData<FirebaseUser?> = _user


    //login status
    private val _loggedIn = Transformations.map(_user) { user ->
        return@map user!=null
    }
    val loggedIn: LiveData<Boolean> = _loggedIn

    fun loggedIn() {
        accountRepo.storeUserToDB()
        _user.postValue(accountRepo.getUser())
    }

    fun logout() {
        accountRepo.logoutUser()
        _user.postValue(null)
    }
}