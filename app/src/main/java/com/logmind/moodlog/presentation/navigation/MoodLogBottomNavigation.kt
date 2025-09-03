package com.logmind.moodlog.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptionsBuilder

@Composable
fun MoodLogBottomNavigation(
    navigate: (String, NavOptionsBuilder.() -> Unit) -> Unit,
    currentRoute: String?,
    startDestinationId: Int,
    showBottomBar: Boolean,
) {
    AnimatedVisibility(
        visible = showBottomBar,
        enter = fadeIn(tween(300)),
        exit = fadeOut(tween(300))
    ) {
        NavigationBar {
            bottomNavigationItems.forEach { item ->
                val selected = currentRoute == item.route

                val iconScale by animateFloatAsState(
                    targetValue = if (selected) 1.2f else 1f,
                    animationSpec = tween(
                        durationMillis = 200,
                        easing = FastOutSlowInEasing
                    ),
                    label = "icon_scale"
                )

                val iconTint by animateColorAsState(
                    targetValue = if (selected) Color.Unspecified else Color.Unspecified,
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
                            contentDescription = item.label,
                            tint = iconTint,
                            modifier = Modifier
                                .scale(iconScale)
                                .size(24.dp)
                        )
                    },
                    label = { Text(item.label) },
                    selected = selected,
                    onClick = {
                        if (currentRoute != item.route) {
                            navigate(item.route) {
                                popUpTo(startDestinationId) {
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