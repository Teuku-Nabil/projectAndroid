package com.example.storyapp.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyapp.model.StoryModel

@Dao
interface UserStoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserStory(story: List<StoryModel>)

    @Query("SELECT * FROM story")
    fun getAllUserStories(): PagingSource<Int, StoryModel>

    @Query("DELETE FROM story")
    suspend fun deleteAllUserStories()
}