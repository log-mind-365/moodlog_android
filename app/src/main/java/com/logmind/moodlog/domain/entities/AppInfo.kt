package com.logmind.moodlog.domain.entities

data class AppInfo(
    val appName: String,
    val packageName: String,
    val version: String,
    val buildNumber: String
)