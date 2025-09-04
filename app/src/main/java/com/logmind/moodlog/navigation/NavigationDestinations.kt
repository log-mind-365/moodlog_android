package com.logmind.moodlog.navigation

import androidx.annotation.StringRes
import com.logmind.moodlog.R
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    val route: String

    @get:StringRes
    val labelResId: Int?
        get() = null

    @Serializable
    data object Home : Screen {
        override val route = "home"
        override val labelResId = R.string.nav_home
    }

    @Serializable
    data object Write : Screen {
        override val route = "write"
        override val labelResId = R.string.nav_write
    }

    @Serializable
    data object Statistics : Screen {
        override val route = "statistics"
        override val labelResId = R.string.nav_statistics
    }

    @Serializable
    data object Settings : Screen {
        override val route = "settings"
        override val labelResId = R.string.nav_settings
    }

    @Serializable
    data object Profile : Screen {
        override val route = "profile"
    }

    @Serializable
    data object Auth : Screen {
        override val route = "auth"

    }

    @Serializable
    data class JournalDetail(val id: Int) : Screen {
        override val route = "journal/$id"
    }


    companion object {
        fun fromRoute(route: String?): Screen {
            return when (route) {
                Home.route -> Home
                Write.route -> Write
                Statistics.route -> Statistics
                Settings.route -> Settings
                Profile.route -> Profile
                Auth.route -> Auth
                else -> Home // Default to Home instead of Auth
            }
        }
    }
}
