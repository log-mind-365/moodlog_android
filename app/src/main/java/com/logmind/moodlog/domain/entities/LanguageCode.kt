package com.logmind.moodlog.domain.entities

enum class LanguageCode(val value: String, val displayName: String) {
    KO("ko", "한국어"),
    EN("en", "English"),
    JA("ja", "日本語"),
    ZH("zh", "中文"),
    ES("es", "Español"),
    IT("it", "Italiano"),
    FR("fr", "Français"),
    VI("vi", "Tiếng Việt"),
    TH("th", "ไทย");

    companion object {
        fun fromString(value: String?): LanguageCode {
            return entries.find { it.value == value } ?: KO
        }
    }
}