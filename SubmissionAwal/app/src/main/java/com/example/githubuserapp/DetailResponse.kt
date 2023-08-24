package com.example.githubuserapp

data class DetailResponse(
    val login: String,
    val avatar_url: String,
    val followers_url: String,
    val following_url: String,
    val id: Int,
    val name: String,
    val followers: String,
    val following: String,
)
