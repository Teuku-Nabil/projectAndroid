package com.tinne14.storyapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.tinne14.storyapp.data.ApiConfig

object Injection {
    fun provideRepository(dataStore: DataStore<Preferences>): Repository {
        val apiService = ApiConfig.getApiService()
        val preference = LoginPreference.getInstance(dataStore)
        return Repository.getInstance(apiService, preference)
    }
}