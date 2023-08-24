package com.tinne14.storyapp

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.tinne14.storyapp.data.ApiConfig
import com.tinne14.storyapp.data.ApiService
import com.tinne14.storyapp.data.LoginResponse
import com.tinne14.storyapp.data.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Flow

class Repository private constructor(
    private val apiService: ApiService,
    private val preference: LoginPreference
) {

    private val TAG = "Repository"

    private val _registerResponse = MutableLiveData<Boolean?>()
    val registerResponse: LiveData<Boolean?> = _registerResponse

    private val _loginResponse = MutableLiveData<Boolean?>()
    val loginResponse: LiveData<Boolean?> = _loginResponse

    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> = _token


    fun postRegister(name: String, email: String, password: String, context: Context) {
        val client = apiService.postRegister(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    _registerResponse.value = response.body()?.error
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_LONG)
                        .show()
                    Log.e(TAG, "Eror: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun postLogin(email: String, password: String, context: Context) {
        val client = apiService.postLogin(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    _token.value = response.body()?.loginResult?.token.toString()
                    _loginResponse.value = response.body()?.error
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_LONG)
                        .show()
                    Log.e(TAG, "Eror: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getToken() = preference.getToken()

    suspend fun saveToken(token: String) {
        preference.saveToken(token)
    }

    suspend fun logout() = preference.logout()

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            preference: LoginPreference
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, preference)
            }.also { instance = it }
    }
}