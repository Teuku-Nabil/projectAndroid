package com.example.storyapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.dataapp.UserRepository
import com.example.storyapp.network.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    private var _error = MutableLiveData<Boolean?>()
    val error: LiveData<Boolean?> = _error

    private var _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message


    fun userRegister(name: String, email: String, password: String) {
        val client = userRepository.userRegister(name, email, password)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _error.value = !response.isSuccessful
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _error.value = true
            }
        })
    }
}