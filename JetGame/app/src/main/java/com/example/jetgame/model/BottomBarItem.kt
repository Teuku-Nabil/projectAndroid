package com.example.jetgame.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.jetgame.navigation.Screen

data class BottomBarItem(
    val title: String,
    val icon: ImageVector,
    val screen: Screen
)
