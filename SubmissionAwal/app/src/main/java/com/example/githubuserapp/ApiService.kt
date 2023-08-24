package com.example.githubuserapp

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getGithub(
        @Query("q") query: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getDetail(
        @Path("username") username: String
    ): Call<DetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<Users>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<Users>>
}