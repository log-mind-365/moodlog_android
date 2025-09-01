package com.logmind.moodlog.domain.entities

enum class AiPersonality(val value: String, val emoji: String) {
    RATIONAL("rational", "🧠"),
    BALANCED("balanced", "⚖️"),
    COMPASSIONATE("compassionate", "💝");

    companion object {
        fun fromString(value: String?): AiPersonality {
            return entries.find { it.value == value } ?: BALANCED
        }
    }
}