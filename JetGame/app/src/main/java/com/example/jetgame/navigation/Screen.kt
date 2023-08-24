package com.example.jetgame.navigation

sealed class Screen(val route: String){
    object Home: Screen("home")
    object About: Screen("about")
    object DetailGame : Screen("home/{GameId}") {
        fun createRoute(GameId: String) = "home/$GameId"
    }
}
