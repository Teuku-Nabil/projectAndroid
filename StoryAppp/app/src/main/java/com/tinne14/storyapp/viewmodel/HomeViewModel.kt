package com.tinne14.storyapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinne14.storyapp.LoginPreference
import com.tinne14.storyapp.Repository
import kotlinx.coroutines.launch

class HomeViewModel( private val mRepository: Repository) : ViewModel(){

    val token: LiveData<String?> = mRepository.token

    fun getToken() = mRepository.getToken()
    suspend fun logout() = mRepository.logout()

}