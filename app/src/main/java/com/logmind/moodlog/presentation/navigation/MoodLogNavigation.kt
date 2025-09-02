package com.logmind.moodlog.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.logmind.moodlog.presentation.home.HomeScreen
import com.logmind.moodlog.presentation.settings.SettingsScreen
import com.logmind.moodlog.presentation.statistics.StatisticsScreen
import com.logmind.moodlog.presentation.write.WriteScreen

@Composable
fun MoodLogNavigation(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val showBottomBar = when (currentRoute) {
        Screen.Write.route -> false
        else -> true
    }
    
    val showFab = when (currentRoute) {
        Screen.Home.route -> true
        Screen.Write.route -> false
        else -> true
    }
    
    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = fadeIn(tween(300)),
                exit = fadeOut(tween(300))
            ) {
                MoodLogBottomNavigation(navController = navController)
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showFab,
                enter = fadeIn(tween(300)),
                exit = fadeOut(tween(300))
            ) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.Write.route)
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                }
            }
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
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
                        },
                    )
                }

                composable(Screen.Statistics.route) {
                    StatisticsScreen()
                }

                composable(Screen.Settings.route) {
                    SettingsScreen(
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
