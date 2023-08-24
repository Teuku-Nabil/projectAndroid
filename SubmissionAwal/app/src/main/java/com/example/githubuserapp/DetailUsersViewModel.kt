package com.example.githubuserapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubuserapp.data.local.FavUser
import com.example.githubuserapp.data.local.FavUserDao
import com.example.githubuserapp.data.local.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUsersViewModel(application: Application) : AndroidViewModel(application) {
    val detailUsers = MutableLiveData<DetailResponse>()

    private var userDao: FavUserDao?
    private var userDb: UserDatabase?

    init {
        userDb = UserDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun setUsersDetail(username: String) {
        ApiConfig.apiService
            .getDetail(username)
            .enqueue(object : Callback<DetailResponse> {
                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    if (response.isSuccessful) {
                        detailUsers.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    fun getUsersDetail(): LiveData<DetailResponse> {
        return detailUsers
    }

    fun addFavorite(username: String, id: Int, avatarUrl: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavUser(
                username,
                id,
                avatarUrl
            )
            userDao?.addFavorite(user)
        }
    }

    suspend fun checkFavorite(id: Int) = userDao?.checkFavorite(id)

    fun removeFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFavorite(id)
        }
    }
}