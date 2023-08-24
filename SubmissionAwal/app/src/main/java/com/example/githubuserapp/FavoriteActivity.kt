package com.example.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.data.local.FavUser
import com.example.githubuserapp.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapterGit: AdapterGit
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapterGit = AdapterGit()
        adapterGit.notifyDataSetChanged()

        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        adapterGit.setOnItemClickCallback(object : AdapterGit.OnItemClickCallback {
            override fun onItemClicked(data: Users) {
                Intent(this@FavoriteActivity, DetailUsersActivity::class.java).also {
                    it.putExtra(DetailUsersActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUsersActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUsersActivity.EXTRA_URL, data.avatar_url)
                    startActivity(it)
                }
            }

        })

        binding.apply {
            rvGit.setHasFixedSize(true)
            rvGit.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvGit.adapter = adapterGit
        }

        viewModel.getFavorite()?.observe(this, {
            if (it != null) {
                val list = mapList(it)
                adapterGit.setUser(list)
            }
        })
    }

    private fun mapList(users: List<FavUser>): ArrayList<Users> {
        val listUsers = ArrayList<Users>()
        for (user in users) {
            val userMapped = Users(
                user.login,
                user.avatar_url,
                user.id
            )
            listUsers.add(userMapped)
        }
        return listUsers
    }
}