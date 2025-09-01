package com.logmind.moodlog.domain.entities

enum class LoginType(val value: String) {
    GOOGLE("google"),
    KAKAO("kakao"),
    ANONYMOUS("anonymous");

    companion object {
        fun fromString(value: String?): LoginType {
            return entries.find { it.value == value } ?: ANONYMOUS
        }
    }
}