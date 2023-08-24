package com.example.storyapp

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import com.example.storyapp.dataapp.UserRepository
import com.example.storyapp.dataapp.datastore.SettingPreference
import com.example.storyapp.database.UserStoryDatabase
import com.example.storyapp.network.ApiConfig
import okhttp3.OkHttpClient

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val preference = SettingPreference.getInstance(context.dataStore)
        val database = UserStoryDatabase.getDatabase(context)
        return UserRepository.getInstance(apiService, preference, database)
    }
}
