package com.logmind.moodlog.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptionsBuilder

@Composable
fun MoodLogBottomNavigation(
    navigate: (String, NavOptionsBuilder.() -> Unit) -> Unit,
    currentScreen: Screen,
    showBottomBar: Boolean,
) {
    AnimatedVisibility(
        visible = showBottomBar,
        enter = fadeIn(tween(300)),
        exit = fadeOut(tween(300))
    ) {
        NavigationBar {
            bottomNavigationItems.forEach { item ->
                val selected = currentScreen.route == item.route
                
                NavigationBarItem(
                    icon = {
                        val icon = if (selected) item.selectedIcon else item.unselectedIcon
                        Icon(
                            imageVector = ImageVector.vectorResource(icon),
                            contentDescription = item.label?.let { stringResource(it) },
                            modifier = Modifier
                                .size(20.dp)
                        )
                    },
                    label = { item.label?.let { Text(stringResource(it)) } },
                    selected = selected,
                    onClick = {
                        if (currentScreen.route != item.route) {
                            navigate(item.route) {
                                popUpTo(Screen.Home.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}