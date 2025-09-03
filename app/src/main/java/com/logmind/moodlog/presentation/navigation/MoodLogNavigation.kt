package com.logmind.moodlog.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.logmind.moodlog.presentation.home.HomeScreen
import com.logmind.moodlog.presentation.settings.SettingsScreen
import com.logmind.moodlog.presentation.statistics.StatisticsScreen
import com.logmind.moodlog.presentation.write.WriteScreen

@Composable
fun MoodLogNavigation(
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.Write.route) {
            WriteScreen(
                navController = navController,
                redirectHome = {
                    navController.navigate(Screen.Home.route)
                },
                onImagePick = {
                    // TODO: Implement image picker
                },
                onCameraTake = {
                    // TODO: Implement camera
                },
            )
        }

        composable(Screen.Statistics.route) {
            StatisticsScreen(navController = navController)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
    }

}
