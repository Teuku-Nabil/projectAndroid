package com.example.storyapp.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.Injection
import com.example.storyapp.dataapp.UserRepository

class ViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(userRepository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(
                userRepository
            ) as T
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(userRepository) as T
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> AddStoryViewModel(
                userRepository
            ) as T
            modelClass.isAssignableFrom(DetailStoryViewModel::class.java) -> DetailStoryViewModel(
                userRepository
            ) as T
            modelClass.isAssignableFrom(MapViewModel::class.java) -> MapViewModel(userRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}
