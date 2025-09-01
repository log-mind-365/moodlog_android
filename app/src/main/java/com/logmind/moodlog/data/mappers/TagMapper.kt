package com.logmind.moodlog.data.mappers

import com.logmind.moodlog.data.database.entities.TagEntity
import com.logmind.moodlog.domain.entities.Tag
import java.time.LocalDateTime

fun TagEntity.toDomainModel(): Tag {
    return Tag(
        id = id,
        name = name,
        color = color,
        createdAt = createdAt
    )
}

fun Tag.toEntity(): TagEntity {
    return TagEntity(
        id = id,
        name = name,
        color = color,
        createdAt = createdAt
    )
}

fun createTagEntity(name: String, color: String?): TagEntity {
    return TagEntity(
        name = name,
        color = color,
        createdAt = LocalDateTime.now()
    )
}