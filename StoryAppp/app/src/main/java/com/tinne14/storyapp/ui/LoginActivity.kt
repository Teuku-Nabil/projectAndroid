package com.tinne14.storyapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.tinne14.storyapp.LoginPreference
import com.tinne14.storyapp.ViewModelFactory
import com.tinne14.storyapp.databinding.ActivityLoginBinding
import com.tinne14.storyapp.viewmodel.LoginViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels{
        ViewModelFactory.getInstance(dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.loginButton.setOnClickListener {
            var email = binding.emailEditText.text.toString().trim()
            var password = binding.passwordEditText.text.toString().trim()
            when {
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Email must not be empty"
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = "Password must not be empty"
                }
                else -> {
                    viewModel.postLogin(email, password, this)
                    viewModel.loginCek.observe(this, {
                        if(it == false){
                            startActivity(Intent(this, HomeActivity::class.java))
                        }
                    })
                }
            }
        }

    }
}