package com.logmind.moodlog.domain.entities

data class LocationInfo(
    val latitude: Double,
    val longitude: Double,
    val address: String?
)