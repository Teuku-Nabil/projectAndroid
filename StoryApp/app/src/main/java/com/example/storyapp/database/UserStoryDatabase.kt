package com.example.storyapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.storyapp.model.StoryModel

@Database(entities = [StoryModel::class, RemoteKeys::class], version = 2, exportSchema = false)
abstract class UserStoryDatabase: RoomDatabase() {

    abstract fun userStoryDao(): UserStoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: UserStoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UserStoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserStoryDatabase::class.java, "story_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}