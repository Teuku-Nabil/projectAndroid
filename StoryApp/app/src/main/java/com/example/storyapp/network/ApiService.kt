package com.example.storyapp.network

import com.example.storyapp.model.DetailStoryModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("login")
    fun userLogin(
        @Body user: Map<String, String>
    ): Call<UserResponse>

    @POST("register")
    fun userRegister(
        @Body user: Map<String, String>
    ): Call<UserResponse>

    @GET("stories")
    fun getUserStory(
        @Query("location") location: Int = 0,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): Call<UserResponse>

    @GET("stories/{id}")
    fun getDetailStory(
        @Path("id") id: String
    ): Call<UserResponse>

    @Multipart
    @POST("stories")
    fun postUserStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float? = null,
        @Part("lon") lon: Float? = null,
    ): Call<UserResponse>
}