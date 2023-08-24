package com.example.storyapp.network

import com.example.storyapp.model.DetailStoryModel
import com.example.storyapp.model.StoryModel
import com.example.storyapp.model.UserModel
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("loginResult")
    val loginResult: UserModel? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("listStory")
    val listStory: ArrayList<StoryModel>,

    @field:SerializedName("story")
    val story: DetailStoryModel? = null,
)
