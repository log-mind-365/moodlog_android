package com.logmind.moodlog.domain.entities

data class UpdateJournalDto(
    val id: Int,
    val content: String?,
    val imageUris: List<String>?,
    val aiResponse: String?,
    val latitude: Double?,
    val longitude: Double?,
    val address: String?
)