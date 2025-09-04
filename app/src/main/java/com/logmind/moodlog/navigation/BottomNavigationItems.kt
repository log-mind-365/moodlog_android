package com.logmind.moodlog.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val label: Int?
)

val bottomNavigationItems = listOf(
    BottomNavigationItem(
        route = Screen.Home.route,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        label = Screen.Home.labelResId
    ),
    BottomNavigationItem(
        route = Screen.Statistics.route,
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Outlined.Star,
        label = Screen.Statistics.labelResId
    ),
    BottomNavigationItem(
        route = Screen.Settings.route,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        label = Screen.Settings.labelResId
    )
)