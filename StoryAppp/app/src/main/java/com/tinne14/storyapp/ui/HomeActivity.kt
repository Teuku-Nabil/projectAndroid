package com.tinne14.storyapp.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.tinne14.storyapp.LoginPreference
import com.tinne14.storyapp.R
import com.tinne14.storyapp.ViewModelFactory
import com.tinne14.storyapp.viewmodel.HomeViewModel
import com.tinne14.storyapp.viewmodel.LoginViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels{
        ViewModelFactory.getInstance(dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var token: TextView = findViewById(R.id.token)
        token.text = viewModel.getToken().toString()

    }
}