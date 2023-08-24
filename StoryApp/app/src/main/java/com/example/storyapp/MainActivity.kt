package com.example.storyapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapp.adapter.LoadingState
import com.example.storyapp.adapter.StoryAdapter
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.model.MainViewModel
import com.example.storyapp.model.StoryModel
import com.example.storyapp.model.ViewModelFactory
import com.example.storyapp.ui.AddStoryActivity
import com.example.storyapp.ui.DetailStoryActivity
import com.example.storyapp.ui.LoginActivity
import com.example.storyapp.ui.MapsActivity

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: StoryAdapter
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(context = this)
    }
    private val recyclerView: RecyclerView
        get() = binding.rvStory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = StoryAdapter()
        adapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: StoryModel) {
                Intent(this@MainActivity, DetailStoryActivity::class.java).also {
                    it.putExtra(DetailStoryActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailStoryActivity.EXTRA_NAME, data.name)
                    it.putExtra(DetailStoryActivity.EXTRA_IMAGE, data.image)
                    it.putExtra(DetailStoryActivity.EXTRA_DESCRIPTION, data.description)
                    startActivity(it)
                }
            }
        })

        viewModel.getFirstTime().observe(this) {
            if (it == false) {
                val msg = getString(R.string.welcome)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        binding.apply {
            rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
            rvStory.adapter = adapter

            binding.refreshLayout.isRefreshing = true
            refreshLayout.setOnRefreshListener {
                adapter.refresh()
            }
            fetchUserStories()
        }
    }

    private fun fetchUserStories() {
        viewModel.getUserToken().observe(this) {
            loadingState(true)
            binding.refreshLayout.isRefreshing = true
            viewModel.listStory(it)
            initRecycler()
            Log.e("Home", "Token: $it")
            loadingState(false)
        }
    }

    private fun initRecycler() {
        val listStoryUser = viewModel.listStory
        listStoryUser.observe(this) {
            updateRecycler(it)
        }
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingState { adapter.retry() }
        )
    }

    private fun updateRecycler(stories: PagingData<StoryModel>) {
        val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager)
            .findLastVisibleItemPosition()
        binding.refreshLayout.isRefreshing = false
        adapter.submitData(lifecycle, stories)
        recyclerView.scrollToPosition(lastVisibleItemPosition)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_story_menu -> {
                val intent = Intent(this, AddStoryActivity::class.java)
                startActivity(intent)
            }
            R.id.action_logout -> {
                viewModel.userLogoutApp()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.action_map -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun loadingState(state: Boolean) {
        binding.loadingIcon.visibility = if (state) View.VISIBLE else View.GONE
    }
}