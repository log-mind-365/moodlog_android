package com.logmind.moodlog.domain.entities

import androidx.compose.ui.graphics.Color

enum class MoodType(val score: Int, val emoji: String, val colorValue: Long) {
    VERY_HAPPY(5, "😄", 0xFF4CAF50),
    HAPPY(4, "😊", 0xFF8BC34A), 
    NEUTRAL(3, "😐", 0xFFFFEB3B),
    SAD(2, "😢", 0xFFFF9800),
    VERY_SAD(1, "😭", 0xFFF44336);

    val color: Color
        get() = Color(colorValue)

    val sliderValue: Float
        get() = when (this) {
            VERY_SAD -> 0.0f
            SAD -> 1.0f
            NEUTRAL -> 2.0f
            HAPPY -> 3.0f
            VERY_HAPPY -> 4.0f
        }

    companion object {
        fun fromSlider(value: Float): MoodType {
            return when (value) {
                0.0f -> VERY_SAD
                1.0f -> SAD
                2.0f -> NEUTRAL
                3.0f -> HAPPY
                4.0f -> VERY_HAPPY
                else -> NEUTRAL
            }
        }

        fun fromScore(score: Int): MoodType {
            return entries.find { it.score == score } ?: NEUTRAL
        }
    }
}