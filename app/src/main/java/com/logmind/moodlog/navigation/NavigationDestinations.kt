package com.logmind.moodlog.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Home : Screen("home")
    object Write : Screen("write")
    object Statistics : Screen("statistics")
    object Settings : Screen("settings")
    object Profile : Screen("profile")

}