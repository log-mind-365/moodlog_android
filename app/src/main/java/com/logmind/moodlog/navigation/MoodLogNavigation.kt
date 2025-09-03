package com.logmind.moodlog.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.logmind.moodlog.R
import com.logmind.moodlog.presentation.auth.AuthScreen
import com.logmind.moodlog.presentation.auth.AuthViewModel
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
            navController = navController,
            topBar = {
                MdlTopAppBar(
                    title = stringResource(R.string.app_name),
                    actions = {
                        MdlAvatar(onClick = {
                            navController.navigate(Screen.Profile.route)
                        })
                    }
                )
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = if (authState.isSignedIn) Screen.Home.route else Screen.Auth.route
            ) {
                composable(Screen.Auth.route) {
                    AuthScreen(
                        onSignInSuccess = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Auth.route) { inclusive = true }
                            }
                        }
                    )
                }
                composable(Screen.Profile.route) {
                    ProfileScreen(navController = navController)
                }
                composable(Screen.Home.route) {
                    HomeScreen()
                }

                composable(Screen.Write.route) {
                    WriteScreen(
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
                    StatisticsScreen()
                }

                composable(Screen.Settings.route) {
                    SettingsScreen()
                }
            }
        }
    }

}
