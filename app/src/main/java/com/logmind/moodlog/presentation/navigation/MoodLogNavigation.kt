package com.logmind.moodlog.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.logmind.moodlog.presentation.home.HomeScreen
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
            HomeScreen(
                onNavigateToWrite = {
                    navController.navigate(Screen.Write.route)
                }
            )
        }

        composable(Screen.Write.route) {
            WriteScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onImagePick = {
                    // TODO: Implement image picker
                },
                onCameraTake = {
                    // TODO: Implement camera
                }
            )
        }

        composable(Screen.Statistics.route) {
            // StatisticsScreen placeholder
        }

        composable(Screen.Settings.route) {
            // SettingsScreen placeholder
        }
    }
}

