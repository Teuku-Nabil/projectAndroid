package com.example.storyapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.dataapp.UserRepository
import com.example.storyapp.network.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddStoryViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUserToken() = userRepository.getUserToken()

    private var _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    fun uploadStory(
        photo: MultipartBody.Part,
        description: RequestBody,
        token: String,
        lat: Float? = null,
        lon: Float? = null
    ) {
        val client = userRepository.uploadStory(photo, description, token, lat, lon)
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