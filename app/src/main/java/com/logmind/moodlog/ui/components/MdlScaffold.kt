package com.logmind.moodlog.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.logmind.moodlog.presentation.navigation.MoodLogBottomNavigation
import com.logmind.moodlog.presentation.navigation.Screen

@Composable
fun MdlScaffold(
    navController: NavHostController,
    topBar: @Composable () -> Unit,
    showFab: Boolean = true,
    showBottomBar: Boolean = true,
    content: @Composable (PaddingValues) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = topBar,
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = fadeIn(tween(300)),
                exit = fadeOut(tween(300))
            ) {
                MoodLogBottomNavigation(
                    currentRoute = currentRoute,
                    showBottomBar = showBottomBar,
                    navigate = { route, navOptionsBuilder ->
                        navController.navigate(
                            route,
                            navOptionsBuilder
                        )
                    },
                    startDestinationId = navController.graph.startDestinationId
                )
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showFab,
                enter = fadeIn(tween(300)),
                exit = fadeOut(tween(300))
            ) {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.Write.route) }
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                }
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}
