package com.logmind.moodlog.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Write : Screen("write")
    object Statistics : Screen("statistics")
    object Settings : Screen("settings")
}