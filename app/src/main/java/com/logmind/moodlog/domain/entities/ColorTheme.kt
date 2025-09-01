package com.logmind.moodlog.domain.entities

enum class ColorTheme(val value: String) {
    RED("red"),
    PINK("pink"),
    PURPLE("purple"),
    DEEP_PURPLE("deepPurple"),
    INDIGO("indigo"),
    BLUE("blue"),
    LIGHT_BLUE("lightBlue"),
    CYAN("cyan"),
    TEAL("teal"),
    GREEN("green"),
    LIGHT_GREEN("lightGreen"),
    LIME("lime"),
    YELLOW("yellow"),
    AMBER("amber"),
    ORANGE("orange"),
    DEEP_ORANGE("deepOrange"),
    BROWN("brown"),
    GREY("grey"),
    BLUE_GREY("blueGrey");

    companion object {
        fun fromString(value: String?): ColorTheme {
            return entries.find { it.value == value } ?: BLUE
        }
    }
}