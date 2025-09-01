package com.logmind.moodlog.domain.entities

enum class FontFamily(val value: String, val fixedFontSize: Float?) {
    PRETENDARD("pretendard", 16.0f),
    LEE_SEOYUN("leeSeoyun", 16.0f),
    ORBIT_OF_THE_MOON("orbitOfTheMoon", 15.0f),
    RESTART("restart", 15.0f),
    OVERCOME("overcome", 16.0f),
    SYSTEM("system", 16.0f);

    companion object {
        fun fromString(value: String?): FontFamily {
            return entries.find { it.value == value } ?: PRETENDARD
        }
    }
}