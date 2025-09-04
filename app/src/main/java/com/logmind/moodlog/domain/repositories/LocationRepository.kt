package com.logmind.moodlog.domain.repositories

import com.logmind.moodlog.domain.entities.LocationInfo

interface LocationRepository {
    suspend fun getCurrentLocation(): Result<LocationInfo>

    suspend fun getAddressFromCoordinates(
        latitude: Double,
        longitude: Double
    ): Result<String>

    suspend fun checkLocationPermission(): Result<Boolean>

    suspend fun requestLocationPermission(): Result<Boolean>
}