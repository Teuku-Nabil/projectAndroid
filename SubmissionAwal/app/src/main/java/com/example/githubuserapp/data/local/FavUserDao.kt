package com.example.githubuserapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavUserDao {
    @Insert
    suspend fun addFavorite(favUser: FavUser)

    @Query("SELECT * FROM users_favorite")
    fun getFavorite(): LiveData<List<FavUser>>

    @Query("SELECT count(*) FROM users_favorite WHERE users_favorite.id = :id")
    suspend fun checkFavorite(id: Int): Int

    @Query("DELETE FROM users_favorite WHERE users_favorite.id = :id")
    suspend fun removeFavorite(id: Int): Int
}