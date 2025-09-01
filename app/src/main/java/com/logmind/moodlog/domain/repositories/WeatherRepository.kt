package com.logmind.moodlog.domain.repositories

import com.logmind.moodlog.domain.common.Result
import com.logmind.moodlog.domain.entities.WeatherCondition
import com.logmind.moodlog.domain.entities.WeatherInfo

interface WeatherRepository {
    /**
     * 현재 날씨 정보를 가져옵니다.
     */
    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double
    ): Result<WeatherInfo>

    /**
     * 날씨 아이콘 코드를 WeatherCondition으로 변환합니다.
     */
    fun getWeatherCondition(iconCode: String): WeatherCondition

    /**
     * 날씨 아이콘 URL을 생성합니다.
     */
    fun getWeatherIconUrl(iconCode: String): String
}