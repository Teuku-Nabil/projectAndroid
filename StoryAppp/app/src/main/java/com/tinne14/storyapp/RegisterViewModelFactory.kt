package com.tinne14.storyapp

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tinne14.storyapp.viewmodel.RegisterViewModel

class RegisterViewModelFactory private constructor(private val mApplication: Repository) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: RegisterViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Repository): RegisterViewModelFactory {
            if (INSTANCE == null) {
                synchronized(RegisterViewModelFactory::class.java) {
                    INSTANCE = RegisterViewModelFactory(application)
                }
            }
            return INSTANCE as RegisterViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}