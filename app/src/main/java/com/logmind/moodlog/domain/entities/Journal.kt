package com.logmind.moodlog.domain.entities

import java.time.LocalDateTime

data class Journal(
    val id: Int,
    val content: String?,
    val moodType: MoodType,
    val imageUris: List<String>?,
    val createdAt: LocalDateTime,
    val aiResponseEnabled: Boolean,
    val aiResponse: String?,
    val latitude: Double?,
    val longitude: Double?,
    val address: String?,
    val temperature: Double?,
    val weatherIcon: String?,
    val weatherDescription: String?,
    val tags: List<Tag>?
)