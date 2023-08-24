package com.example.storyapp.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ActivityDetailStoryBinding
import com.example.storyapp.model.DetailStoryViewModel
import com.example.storyapp.model.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private val viewModel: DetailStoryViewModel by viewModels {
        ViewModelFactory.getInstance(context = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_ID)

        viewModel.getUserToken().observe(this) {
            viewModel.detailListStory(it, id.toString())
            loadingState(true)
            if (viewModel.error.value == true) {
                loadingState(false)
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.getDetailStory().observe(this) { story ->
                    if (story != null) {
                        loadingState(false)
                        binding.apply {
                            binding.tvName.text = story.name
                            binding.tvDescription.text = story.description
                            Glide.with(this@DetailStoryActivity)
                                .load(story.image)
                                .into(ivImageStory)
                        }
                    } else {
                        Log.e("DetailStoryActivity", "Data tidak ditemukan")
                        loadingState(false)
                    }
                }
            }
        }
    }

    private fun loadingState(state: Boolean) {
        binding.loadingIcon.visibility = if (state) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_DESCRIPTION = "extra_description"
    }
}