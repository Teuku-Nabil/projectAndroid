package com.tinne14.storyapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinne14.storyapp.LoginPreference
import com.tinne14.storyapp.Repository
import kotlinx.coroutines.launch


class LoginViewModel( private val mRepository: Repository) : ViewModel(){

    val loginCek: LiveData<Boolean?> = mRepository.loginResponse
    val token: LiveData<String?> = mRepository.token

    fun postLogin(email: String, password: String, context: Context){
        viewModelScope.launch {
            mRepository.postLogin(email, password, context)
            mRepository.saveToken(token.toString())
        }
    }

}