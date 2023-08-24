package com.example.storyapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.dataapp.UserRepository
import com.example.storyapp.network.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailStoryViewModel(private val userRepository: UserRepository) : ViewModel() {
    val detailStory = MutableLiveData<DetailStoryModel>()

    private var _error = MutableLiveData<Boolean?>()
    val error: LiveData<Boolean?> = _error

    fun getUserToken() = userRepository.getUserToken()

    fun detailListStory(token: String, id: String) {
        userRepository.userStory(token)
            .getDetailStory(id)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()?.story
                        detailStory.postValue(responseBody!!)
                        _error.value = false
                        Log.e("DetailStory", "Success")
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    fun getDetailStory(): LiveData<DetailStoryModel> {
        return detailStory
    }
}