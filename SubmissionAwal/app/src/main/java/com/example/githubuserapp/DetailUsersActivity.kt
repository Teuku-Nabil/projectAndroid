package com.example.githubuserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githubuserapp.databinding.ActivityDetailUsersBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUsersActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
    }

    private lateinit var binding: ActivityDetailUsersBinding
    private lateinit var viewModel: DetailUsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_URL)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel = ViewModelProvider(this)[DetailUsersViewModel::class.java]

        viewModel.setUsersDetail(username!!)
        showLoading(true)
        viewModel.getUsersDetail().observe(this@DetailUsersActivity) {
            if (it != null) {
                showLoading(false)
                binding.apply {
                    tvName.text = it.login
                    tvUsername.text = it.name
                    followersCount.text = "${it.followers} Followers"
                    followingCount.text = "${it.following} Following"
                    Glide.with(this@DetailUsersActivity)
                        .load(it.avatar_url)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(ivProfile)
                }
            }
        }

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkFavorite(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.toogleFav.isChecked = true
                        _isChecked = true
                    } else {
                        binding.toogleFav.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        binding.toogleFav.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked) {
                viewModel.addFavorite(username, id, avatarUrl)
            } else {
                viewModel.removeFavorite(id)
            }
            binding.toogleFav.isChecked = _isChecked
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, bundle)
        binding.apply {
            viewPager.adapter = sectionsPagerAdapter
            tabs.setupWithViewPager(viewPager)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

}