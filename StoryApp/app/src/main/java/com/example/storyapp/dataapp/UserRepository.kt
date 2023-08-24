package com.example.storyapp.dataapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.*
import com.example.storyapp.dataapp.datastore.SettingPreference
import com.example.storyapp.dataapp.paging.UserStoryRemoteMediator
import com.example.storyapp.database.UserStoryDatabase
import com.example.storyapp.model.StoryModel
import com.example.storyapp.network.ApiInterceptor
import com.example.storyapp.network.ApiService
import com.example.storyapp.network.UserResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRepository(
    private val apiService: ApiService,
    private val pref: SettingPreference,
    private val database: UserStoryDatabase,
) {
    fun getUserToken(): LiveData<String> = pref.getUserToken().asLiveData()

    suspend fun saveUserToken(value: String) = pref.saveUserToken(value)

    suspend fun saveUserName(value: String) = pref.saveUserName(value)

    fun getFirstTime(): LiveData<Boolean> = pref.getFirstTime().asLiveData()

    suspend fun login() = pref.login()

    suspend fun logout() = pref.logout()

    fun userLogin(email: String, password: String): Call<UserResponse> {
        val user: Map<String, String> = mapOf(
            "email" to email,
            "password" to password
        )
        return apiService.userLogin(user)
    }

    fun userRegister(name: String, email: String, password: String): Call<UserResponse> {
        val user: Map<String, String> = mapOf(
            "name" to name,
            "email" to email,
            "password" to password
        )
        return apiService.userRegister(user)
    }

    fun userStory(token: String): ApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(ApiInterceptor(token))
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    fun uploadStory(
        photo: MultipartBody.Part,
        description: RequestBody,
        token: String,
        lat: Float? = null,
        lon: Float? = null
    ): Call<UserResponse> = userStory(token).postUserStory(photo, description, lat, lon)

    fun detailUserStory(
        token: String,
        id: String
    ): Call<UserResponse> = userStory(token).getDetailStory(id)

    fun getUserStoryMapView(token: String): Call<UserResponse> {
        return userStory(token).getUserStory(1)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getUserStoryList(token: String): LiveData<PagingData<StoryModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            remoteMediator = UserStoryRemoteMediator(
                database,
                apiService = userStory(token)
            ),
            pagingSourceFactory = { database.userStoryDao().getAllUserStories() }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            apiService: ApiService,
            pref: SettingPreference,
            database: UserStoryDatabase
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, pref, database).also { instance = it }
            }
    }
}