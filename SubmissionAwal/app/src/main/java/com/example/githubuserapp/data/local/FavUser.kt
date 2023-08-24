package com.example.githubuserapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "users_favorite")
data class FavUser(
    val login: String,
    @PrimaryKey
    val id: Int,
    val avatar_url: String?
) : Serializable
