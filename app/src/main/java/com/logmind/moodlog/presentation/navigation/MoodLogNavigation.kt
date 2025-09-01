package com.logmind.moodlog.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.logmind.moodlog.presentation.home.HomeScreen

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
            // WriteScreen will be implemented
            WriteScreenPlaceholder(
                onNavigateBack = {
                    navController.popBackStack()
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

@Composable
fun WriteScreenPlaceholder(
    onNavigateBack: () -> Unit
) {
    androidx.compose.foundation.layout.Column {
        androidx.compose.material3.Text("Write Screen - Coming Soon!")
        androidx.compose.material3.Button(onClick = onNavigateBack) {
            androidx.compose.material3.Text("Back")
        }
    }
}