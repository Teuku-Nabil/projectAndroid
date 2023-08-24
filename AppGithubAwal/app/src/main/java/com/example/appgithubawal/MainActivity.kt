package com.example.appgithubawal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appgithubawal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAdapter = UserAdapter()
        userAdapter.notifyDataSetChanged()
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        if (savedInstanceState == null) {
            showLoading(true)
            mainViewModel.setSearchUser("a")
        }

        binding.apply {
            rvUsers.setHasFixedSize(true)
            rvUsers.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUsers.adapter = userAdapter

            ivSearch.setOnClickListener {
                searchUsers()
            }

            etQuery.setOnKeyListener { v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchUsers()
                    return@setOnKeyListener true
                }else {
                    return@setOnKeyListener false
                }
            }

            mainViewModel.getUsers().observe(this@MainActivity) { userItems ->
                if (userItems != null) {
                    userAdapter.setData(userItems)
                    showLoading(false)
                }
            }
        }
    }

    private fun searchUsers() {
        binding.apply {
            val query = etQuery.text.toString()
            if (query.isEmpty()){
                Toast.makeText(this@MainActivity, "Please input username", Toast.LENGTH_SHORT).show()
            }
            showLoading(true)
            mainViewModel.setSearchUser(query)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}