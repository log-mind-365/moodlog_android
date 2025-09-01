package com.logmind.moodlog.domain.entities

import androidx.compose.ui.text.style.TextAlign

enum class SimpleTextAlign(val value: String, val textAlign: TextAlign) {
    LEFT("left", TextAlign.Left),
    CENTER("center", TextAlign.Center),
    RIGHT("right", TextAlign.Right);

    fun next(): SimpleTextAlign {
        val currentIndex = entries.indexOf(this)
        val nextIndex = (currentIndex + 1) % entries.size
        return entries[nextIndex]
    }

    companion object {
        fun fromString(value: String?): SimpleTextAlign {
            return entries.find { it.value == value } ?: LEFT
        }
    }
}