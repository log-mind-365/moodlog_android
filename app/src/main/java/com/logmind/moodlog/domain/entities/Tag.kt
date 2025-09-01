package com.logmind.moodlog.domain.entities

import java.time.LocalDateTime

data class Tag(
    val id: Int,
    val name: String,
    val color: String?,
    val createdAt: LocalDateTime
)