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
import com.tinne14.storyapp.RegisterViewModelFactory
import com.tinne14.storyapp.Repository
import com.tinne14.storyapp.ViewModelFactory
import com.tinne14.storyapp.databinding.ActivityRegisterBinding
import com.tinne14.storyapp.viewmodel.RegisterViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels{
        ViewModelFactory.getInstance(dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.signupButton.setOnClickListener {
            val nama = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            when {
                nama.isEmpty() -> {
                    binding.nameEditTextLayout.error = "Name must not be empty"
                }
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Email must not be empty"
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = "Password must not be empty"
                }
                else -> {
                    viewModel.postRegister(nama, email, password, this)
                    viewModel.registerCek.observe(this, {
                        if(it == false){
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                    })
                }
            }

        }


    }
}
