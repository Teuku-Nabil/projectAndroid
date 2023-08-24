package com.tinne14.storyapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinne14.storyapp.Repository
import kotlinx.coroutines.launch

class RegisterViewModel (private val mRepository: Repository) : ViewModel(){

    val registerCek: LiveData<Boolean?> = mRepository.registerResponse

    fun postRegister(name: String, email: String, password: String, context: Context){
        viewModelScope.launch {
            mRepository.postRegister(name, email, password, context)
        }
    }

}