package com.logmind.moodlog.navigation

import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.logmind.moodlog.R
import com.logmind.moodlog.presentation.auth.AuthScreen
import com.logmind.moodlog.presentation.auth.AuthViewModel
import com.logmind.moodlog.presentation.entries.EntriesScreen
import com.logmind.moodlog.presentation.home.HomeScreen
import com.logmind.moodlog.presentation.profile.ProfileScreen
import com.logmind.moodlog.presentation.settings.SettingsScreen
import com.logmind.moodlog.presentation.statistics.StatisticsScreen
import com.logmind.moodlog.presentation.write.WriteScreen
import com.logmind.moodlog.ui.components.MdlAvatar
import com.logmind.moodlog.ui.components.MdlScaffold
import com.logmind.moodlog.ui.components.MdlTopAppBar

@Composable
fun MoodLogNavigation(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()

    CompositionLocalProvider(LocalContext provides context) {
        MdlScaffold(
            navController = navController
        ) { innerPadding ->
            val modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    horizontal = dimensionResource(
                        R.dimen.horizontal_padding
                    )
                )

            NavHost(
                navController = navController,
                startDestination = if (authState.isSignedIn) Screen.Home.route else Screen.Auth.route,
                enterTransition = {
                    slideInVertically(
                        animationSpec = tween(easing = Ease),
                        initialOffsetY = { 80 }
                    ) + fadeIn()
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        Screen.Write.route -> slideOutHorizontally(
                            animationSpec = tween(easing = Ease),
                            targetOffsetX = { -it / 2 }
                        )

                        else -> fadeOut(
                            animationSpec = tween(easing = Ease)
                        )
                    }
                },
                popEnterTransition = {
                    slideInHorizontally(
                        animationSpec = tween(easing = Ease),
                        initialOffsetX = { -it }
                    )
                },
                popExitTransition = {
                    fadeOut(
                        animationSpec = tween(easing = Ease)
                    )
                }

            ) {
                composable(Screen.Auth.route) {
                    AuthScreen(
                        modifier = modifier,
                        onSignInSuccess = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Auth.route) { inclusive = true }
                            }
                        }
                    )
                }
                composable(Screen.Profile.route) {
                    ProfileScreen(modifier = modifier, navController = navController)
                }
                composable(Screen.Home.route) {
                    HomeScreen(
                        modifier = modifier,
                        topAppBar = {
                            MdlTopAppBar(
                                title = R.string.app_name,
                                actions = {
                                    MdlAvatar(onClick = {
                                        navController.navigate(Screen.Profile.route)
                                    })
                                }
                            )
                        }
                    )
                }
                composable(Screen.Entries.route) {
                    EntriesScreen(modifier = modifier, topAppBar = {
                        MdlTopAppBar(
                            title = R.string.app_name,
                            actions = {
                                MdlAvatar(onClick = {
                                    navController.navigate(Screen.Profile.route)
                                })
                            }
                        )
                    })
                }
                composable(
                    Screen.Write.route,
                    enterTransition = {
                        slideInHorizontally(
                            animationSpec = tween(easing = Ease),
                            initialOffsetX = { it }
                        )
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            animationSpec = tween(easing = Ease),
                            targetOffsetX = { it },
                        )
                    },
                ) {
                    WriteScreen(
                        modifier = modifier,
                        redirectHome = {
                            navController.navigate(Screen.Home.route)
                        },
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
                    StatisticsScreen(modifier = modifier, topAppBar = {
                        MdlTopAppBar(
                            title = R.string.app_name,
                            actions = {
                                MdlAvatar(onClick = {
                                    navController.navigate(Screen.Profile.route)
                                })
                            }
                        )
                    })
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(modifier = modifier, topAppBar = {
                        MdlTopAppBar(
                            title = R.string.app_name,
                            actions = {
                                MdlAvatar(onClick = {
                                    navController.navigate(Screen.Profile.route)
                                })
                            }
                        )
                    })
                }
            }
        }
    }

}
