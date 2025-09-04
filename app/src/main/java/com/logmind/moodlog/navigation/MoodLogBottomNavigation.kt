package com.logmind.moodlog.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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

                val iconTint by animateColorAsState(
                    targetValue = if (selected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else Color.Unspecified,
                    animationSpec = tween(
                        durationMillis = 200,
                        easing = FastOutSlowInEasing
                    ),
                    label = "icon_tint"
                )

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label?.let { stringResource(it) },
                            tint = iconTint,
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