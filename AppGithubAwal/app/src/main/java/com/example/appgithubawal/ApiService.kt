package com.example.appgithubawal

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun searchUsers(@Query("q") query: String): Call<GithubResponse>
}