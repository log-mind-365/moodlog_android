package com.logmind.moodlog.domain.entities

import java.time.LocalDateTime

data class CreateJournalDto(
    val content: String?,
    val moodType: MoodType,
    val imageUris: List<String>?,
    val aiResponseEnabled: Boolean,
    val aiResponse: String?,
    val createdAt: LocalDateTime,
    val latitude: Double?,
    val longitude: Double?,
    val address: String?,
    val temperature: Double?,
    val weatherIcon: String?,
    val weatherDescription: String?
)