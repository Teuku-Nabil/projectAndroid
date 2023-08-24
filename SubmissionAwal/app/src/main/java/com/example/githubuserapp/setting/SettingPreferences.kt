package com.example.githubuserapp.setting

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getTheme(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    suspend fun saveTheme(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    companion object {
        private val THEME_KEY = booleanPreferencesKey("theme_setting")

        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<androidx.datastore.preferences.core.Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}