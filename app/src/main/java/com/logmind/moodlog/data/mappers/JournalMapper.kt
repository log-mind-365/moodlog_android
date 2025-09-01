package com.logmind.moodlog.data.mappers

import com.logmind.moodlog.data.database.entities.JournalEntity
import com.logmind.moodlog.domain.entities.CreateJournalDto
import com.logmind.moodlog.domain.entities.Journal
import com.logmind.moodlog.domain.entities.Tag
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun JournalEntity.toDomainModel(tags: List<Tag>? = null): Journal {
    val gson = Gson()
    val imageUrisList = imageUris?.let {
        if (it.isBlank()) null else {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson<List<String>>(it, type)
        }
    }
    
    return Journal(
        id = id,
        content = content,
        moodType = moodType,
        imageUris = imageUrisList,
        createdAt = createdAt,
        aiResponseEnabled = aiResponseEnabled,
        aiResponse = aiResponse,
        latitude = latitude,
        longitude = longitude,
        address = address,
        temperature = temperature,
        weatherIcon = weatherIcon,
        weatherDescription = weatherDescription,
        tags = tags
    )
}

fun CreateJournalDto.toEntity(): JournalEntity {
    val gson = Gson()
    val imageUrisJson = imageUris?.takeIf { it.isNotEmpty() }?.let { gson.toJson(it) }
    
    return JournalEntity(
        content = content,
        moodType = moodType,
        imageUris = imageUrisJson,
        createdAt = createdAt,
        aiResponseEnabled = aiResponseEnabled,
        aiResponse = aiResponse,
        latitude = latitude,
        longitude = longitude,
        address = address,
        temperature = temperature,
        weatherIcon = weatherIcon,
        weatherDescription = weatherDescription
    )
}