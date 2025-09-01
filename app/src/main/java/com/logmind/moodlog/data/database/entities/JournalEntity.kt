package com.logmind.moodlog.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.logmind.moodlog.domain.entities.MoodType
import java.time.LocalDateTime

@Entity(tableName = "journals")
data class JournalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    @ColumnInfo(name = "content")
    val content: String?,
    
    @ColumnInfo(name = "mood_type")
    val moodType: MoodType,
    
    @ColumnInfo(name = "image_uris")
    val imageUris: String?, // JSON string
    
    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime,
    
    @ColumnInfo(name = "ai_response_enabled")
    val aiResponseEnabled: Boolean,
    
    @ColumnInfo(name = "ai_response")
    val aiResponse: String?,
    
    @ColumnInfo(name = "latitude")
    val latitude: Double?,
    
    @ColumnInfo(name = "longitude")
    val longitude: Double?,
    
    @ColumnInfo(name = "address")
    val address: String?,
    
    @ColumnInfo(name = "temperature")
    val temperature: Double?,
    
    @ColumnInfo(name = "weather_icon")
    val weatherIcon: String?,
    
    @ColumnInfo(name = "weather_description")
    val weatherDescription: String?
)