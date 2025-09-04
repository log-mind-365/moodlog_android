package com.logmind.moodlog.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
) {
    NavigationBar {
        bottomNavigationItems.forEach { item ->
            val selected = currentScreen.route == item.route
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
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
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}