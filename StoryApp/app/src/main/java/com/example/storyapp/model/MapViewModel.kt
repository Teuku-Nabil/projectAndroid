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

class MapViewModel(private val userRepository: UserRepository) : ViewModel() {
    private var _userStory = MutableLiveData<ArrayList<StoryModel>>()
    val userStory: LiveData<ArrayList<StoryModel>> = _userStory

    fun getUserToken(): LiveData<String> = userRepository.getUserToken()

    fun getUserStoryWithLocation(token: String) {
        val client = userRepository.getUserStoryMapView(token)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()?.listStory
                    _userStory.postValue(userResponse!!)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("Failure", t.message.toString())
            }
        })
    }
}