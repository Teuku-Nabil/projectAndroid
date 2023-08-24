package com.azhar.githubusers.networking

import com.azhar.githubusers.model.follow.ModelFollow
import com.azhar.githubusers.model.search.ModelSearch
import com.azhar.githubusers.model.user.ModelUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Azhar Rivaldi on 18-05-2021
 * Youtube Channel : https://bit.ly/2PJMowZ
 * Github : https://github.com/AzharRivaldi
 * Twitter : https://twitter.com/azharrvldi_
 * Instagram : https://www.instagram.com/azhardvls_
 * Linkedin : https://www.linkedin.com/in/azhar-rivaldi
 */
interface ApiInterface {
    @GET("users/{username}")
    fun detailUser(@Path("username") username: String?): Call<ModelUser?>?

    @GET("/search/users")
    fun searchUser(
        @Header("Authorization") authorization: String?,
        @Query("q") username: String?
    ): Call<ModelSearch?>?

    @GET("users/{username}/followers")
    fun followersUser(
        @Header("Authorization") authorization: String?,
        @Path("username") username: String?
    ): Call<List<ModelFollow?>?>?

    @GET("users/{username}/following")
    fun followingUser(
        @Header("Authorization") authorization: String?,
        @Path("username") username: String?
    ): Call<List<ModelFollow?>?>?
}