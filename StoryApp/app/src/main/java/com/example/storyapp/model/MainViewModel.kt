package com.example.storyapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.dataapp.UserRepository
import com.example.storyapp.network.UserResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    lateinit var listStory: LiveData<PagingData<StoryModel>>

    fun getUserToken() = userRepository.getUserToken()

    fun getFirstTime(): LiveData<Boolean> = userRepository.getFirstTime()

    fun listStory(token: String) {
        listStory = userRepository.getUserStoryList(token).cachedIn(viewModelScope)
    }

    fun listUserStory(token: String): LiveData<PagingData<StoryModel>> =
        userRepository.getUserStoryList(token).cachedIn(viewModelScope)

    fun userLogoutApp() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}