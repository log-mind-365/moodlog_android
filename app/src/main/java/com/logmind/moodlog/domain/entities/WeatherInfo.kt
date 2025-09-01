package com.logmind.moodlog.domain.entities

import java.time.LocalDateTime

data class WeatherInfo(
    val temperature: Double,
    val description: String,
    val icon: String,
    val humidity: Double,
    val pressure: Double,
    val windSpeed: Double,
    val location: String,
    val timestamp: LocalDateTime
)

enum class WeatherCondition(val displayName: String, val icon: String) {
    CLEAR("맑음", "☀️"),
    CLOUDY("흐림", "☁️"),
    RAINY("비", "🌧️"),
    SNOWY("눈", "❄️"),
    THUNDERSTORM("뇌우", "⛈️"),
    FOG("안개", "🌫️"),
    UNKNOWN("알 수 없음", "❓")
}