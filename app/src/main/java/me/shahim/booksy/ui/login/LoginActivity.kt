package me.shahim.booksy.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import me.shahim.booksy.MainActivity
import me.shahim.booksy.R
import me.shahim.booksy.databinding.ActivityLoginBinding
import me.shahim.booksy.databinding.ActivityMainBinding
import me.shahim.booksy.ui.account.AccountViewModel


class LoginActivity : AppCompatActivity() {

    companion object {
        const val RC_AUTH = 100
    }

    private lateinit var binding: ActivityLoginBinding

    private lateinit var mainViewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        init()
    }

    private fun init() {

    }


}

