package com.example.storyapp.model

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.dataapp.UserRepository
import com.example.storyapp.network.UserResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    private var _user = MutableLiveData<UserModel>()
    val user: LiveData<UserModel> = _user

    private var _error = MutableLiveData<Boolean?>()
    val error: LiveData<Boolean?> = _error

    fun userLoginApp() {
        viewModelScope.launch {
            userRepository.login()
        }
    }

    fun userLogin(email: String, password: String) {
        val client = userRepository.userLogin(email, password)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.loginResult
                    _error.value = false
                    _user.postValue(responseBody!!)
                } else {
                    _error.value = true
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _error.value = true
            }
        })
    }

    fun saveUserToken(token: String?) {
        viewModelScope.launch {
            if (token != null) {
                userRepository.saveUserToken(token)
            }
        }
    }

    fun saveUserName(name: String?) {
        viewModelScope.launch {
            if (name != null) {
                userRepository.saveUserName(name)
            }
        }
    }
}