package com.example.appgithubawal

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    val listUser = MutableLiveData<ArrayList<User>>()

    fun setSearchUser(query: String) {
        ApiConfig.apiService
            .searchUsers(query)
            .enqueue(object : Callback<GithubResponse> {
                override fun onResponse(
                    call: Call<GithubResponse>,
                    response: Response<GithubResponse>
                ) {
                    if (response.isSuccessful) {
                        listUser.postValue(response.body()?.items as ArrayList<User>?)
                    }
                }

                override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    fun getUsers(): MutableLiveData<ArrayList<User>> {
        return listUser
    }
}