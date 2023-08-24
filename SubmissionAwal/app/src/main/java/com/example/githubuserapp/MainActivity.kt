package com.example.githubuserapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.databinding.ActivityMainBinding
import com.example.githubuserapp.setting.SettingActivity
import androidx.datastore.preferences.core.Preferences
import com.example.githubuserapp.setting.SettingPreferences

class MainActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: AdapterGit
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAdapter = AdapterGit()
        userAdapter.notifyDataSetChanged()

        userAdapter.setOnItemClickCallback(object : AdapterGit.OnItemClickCallback {
            override fun onItemClicked(data: Users) {
                Intent(this@MainActivity, DetailUsersActivity::class.java).also {
                    it.putExtra(DetailUsersActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUsersActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUsersActivity.EXTRA_URL, data.avatar_url)
                    startActivity(it)
                }
            }

        })

        val pref = SettingPreferences.getInstance(dataStore)
        mainViewModel = ViewModelProvider(
            this, MainViewModelFactory(pref)
        )[MainViewModel::class.java]

        if (savedInstanceState == null) {
            showLoading(true)
            mainViewModel.findUsers("a")
        }

        mainViewModel.getTheme().observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.apply {
            rvGit.setHasFixedSize(true)
            rvGit.layoutManager = LinearLayoutManager(this@MainActivity)
            rvGit.adapter = userAdapter

            btnSearch.setOnClickListener {
                searchUsers()
            }

            etQuery.setOnKeyListener { v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchUsers()
                    return@setOnKeyListener true
                } else {
                    return@setOnKeyListener false
                }
            }

            mainViewModel.getUsers().observe(this@MainActivity) { userItems ->
                if (userItems != null) {
                    userAdapter.setUser(userItems)
                    showLoading(false)
                }
            }
        }
    }

    private fun searchUsers() {
        binding.apply {
            val query = etQuery.text.toString()
            if (query.isEmpty()) {
                Toast.makeText(this@MainActivity, "Input username", Toast.LENGTH_SHORT).show()
            }
            showLoading(true)
            mainViewModel.findUsers(query)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite_menu -> {
                Intent(this, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }

            R.id.setting_menu -> {
                Intent(this, SettingActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}