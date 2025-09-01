package com.logmind.moodlog.domain.entities

enum class ThemeMode(val value: String, val displayName: String) {
    LIGHT("light", "Light"),
    DARK("dark", "Dark"),
    SYSTEM("system", "System");

    companion object {
        fun fromString(value: String?): ThemeMode {
            return entries.find { it.value == value } ?: SYSTEM
        }
    }
}