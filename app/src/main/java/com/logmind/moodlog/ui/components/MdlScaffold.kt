package com.logmind.moodlog.ui.components

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.logmind.moodlog.R
import com.logmind.moodlog.navigation.MoodLogBottomNavigation
import com.logmind.moodlog.navigation.Screen

@Composable
fun MdlScaffold(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentScreen = Screen.fromRoute(currentRoute)
    var showBottomBar by remember { mutableStateOf(true) }
    var showFab by remember { mutableStateOf(true) }

    LaunchedEffect(currentScreen) {
        showBottomBar = when (currentScreen) {
            is Screen.Write -> false
            else -> true
        }
        showFab = when (currentScreen) {
            is Screen.Write -> false
            else -> true
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            when (currentScreen) {
                is Screen.Home -> MdlTopAppBar(
                    title = stringResource(R.string.app_name),
                    actions = {
                        MdlAvatar(onClick = {
                            navController.navigate(Screen.Profile.route)
                        })
                    }
                )

                is Screen.Statistics -> MdlTopAppBar(
                    title = stringResource(R.string.nav_statistics)
                )

                is Screen.Settings -> MdlTopAppBar(
                    title = stringResource(R.string.nav_settings)
                )

                is Screen.Write -> MdlTopAppBar(
                    navigationIcon = {
                        MdlBackButton(onClick = { navController.popBackStack() })
                    }
                )

                else -> MdlTopAppBar(
                    title = stringResource(R.string.app_name)
                )
            }
        },
        bottomBar = {
            MoodLogBottomNavigation(
                currentScreen = currentScreen,
                showBottomBar = showBottomBar,
                navigate = { route, navOptionsBuilder ->
                    navController.navigate(
                        route,
                        navOptionsBuilder
                    )
                }
            )
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
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = dimensionResource(R.dimen.horizontal_padding))
        ) {
            content()
        }
    }
}
