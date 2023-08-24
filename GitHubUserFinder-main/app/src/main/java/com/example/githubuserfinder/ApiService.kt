package com.example.githubuserfinder

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun searchUsers(@Query("q") query: String): Call<GithubResponse>
}