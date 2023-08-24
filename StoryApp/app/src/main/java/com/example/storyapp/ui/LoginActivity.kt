package com.example.storyapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapp.MainActivity
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.model.LoginViewModel
import com.example.storyapp.model.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(context = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        val edEmail = binding.edLoginEmail
        val edPassword = binding.edLoginPassword

        binding.loginButton.setOnClickListener {
            val email = edEmail.text.toString()
            val password = edPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                val msg = getString(R.string.fill_field)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            } else if (password.length < 8 || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                val msg = getString(R.string.password_email_error)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            } else {
                loginViewModel.userLogin(email, password)
                loadingState(true)
                loginViewModel.error.observe(this) { error ->
                    if (error == false) {
                        loadingState(false)
                        val msg = getString(R.string.login_success)
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                        loginViewModel.userLoginApp()
                        loginViewModel.user.observe(this) { user ->
                            if (user != null) {
                                loginViewModel.saveUserToken(user.token)
                                loginViewModel.saveUserName(user.name)
                            }
                        }
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        loadingState(false)
                        val msg = getString(R.string.login_failed)
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        binding.tvToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadingState(state: Boolean) {
        binding.loadingIcon.visibility = if (state) View.VISIBLE else View.GONE
    }
}