package com.logmind.moodlog.navigation

import androidx.annotation.DrawableRes
import com.logmind.moodlog.R

data class BottomNavigationItem(
    val route: String,
    @param:DrawableRes val selectedIcon: Int,
    @param:DrawableRes val unselectedIcon: Int,
    val label: Int?
)

val bottomNavigationItems = listOf(
    BottomNavigationItem(
        route = Screen.Home.route,
        selectedIcon = R.drawable.ic_filled_home,
        unselectedIcon = R.drawable.ic_outline_home,
        label = Screen.Home.labelResId
    ),
    BottomNavigationItem(
        route = Screen.Entries.route,
        selectedIcon = R.drawable.ic_filled_book,
        unselectedIcon = R.drawable.ic_outline_book,
        label = Screen.Entries.labelResId
    ),
    BottomNavigationItem(
        route = Screen.Statistics.route,
        selectedIcon = R.drawable.ic_base_stat,
        unselectedIcon = R.drawable.ic_base_stat,
        label = Screen.Statistics.labelResId
    ),
    BottomNavigationItem(
        route = Screen.Settings.route,
        selectedIcon = R.drawable.ic_filled_setting,
        unselectedIcon = R.drawable.ic_outline_setting,
        label = Screen.Settings.labelResId
    )
)