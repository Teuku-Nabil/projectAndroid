package com.example.githubuserapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {
    val listFollowers = MutableLiveData<ArrayList<Users>>()

    fun setFollowersList(username: String) {
        ApiConfig.apiService
            .getFollowers(username)
            .enqueue(object : Callback<ArrayList<Users>> {
                override fun onResponse(
                    call: Call<ArrayList<Users>>,
                    response: Response<ArrayList<Users>>
                ) {
                    if (response.isSuccessful) {
                        listFollowers.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<Users>>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }

            })
    }

    fun getFollowersList(): LiveData<ArrayList<Users>> {
        return listFollowers
    }
}