package com.example.githubuserapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.githubuserapp.setting.SettingPreferences
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {
    val listUser = MutableLiveData<ArrayList<Users>>()

    fun findUsers(query: String) {
        ApiConfig.apiService
            .getGithub(query)
            .enqueue(object : Callback<GithubResponse> {
                override fun onResponse(
                    call: Call<GithubResponse>,
                    response: Response<GithubResponse>
                ) {
                    if (response.isSuccessful) {
                        listUser.postValue(response.body()?.items as ArrayList<Users>?)
                    }
                }

                override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    fun getTheme(): LiveData<Boolean> {
        return pref.getTheme().asLiveData()
    }

    fun getUsers(): MutableLiveData<ArrayList<Users>> {
        return listUser
    }
}