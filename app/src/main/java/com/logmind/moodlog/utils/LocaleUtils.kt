package com.logmind.moodlog.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.logmind.moodlog.domain.entities.LanguageCode
import java.util.*

val LocalLanguage = staticCompositionLocalOf { LanguageCode.KO }

fun Context.setLocale(languageCode: LanguageCode): Context {
    val locale = languageCode.toLocale()
    Locale.setDefault(locale)
    
    val config = Configuration(this.resources.configuration)
    config.setLocale(locale)
    
    return this.createConfigurationContext(config)
}

fun Context.getCurrentLocale(): Locale {
    return resources.configuration.locales[0]
}

@Composable
fun LocaleProvider(
    languageCode: LanguageCode,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val localizedContext = remember(languageCode) {
        context.setLocale(languageCode)
    }
    
    CompositionLocalProvider(
        LocalLanguage provides languageCode,
        LocalContext provides localizedContext
    ) {
        content()
    }
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

fun LanguageCode.toLocale(): Locale {
    return when (this) {
        LanguageCode.KO -> Locale.Builder().setLanguage("ko").setRegion("KR").build()
        LanguageCode.EN -> Locale.Builder().setLanguage("en").setRegion("US").build()
        LanguageCode.JA -> Locale.Builder().setLanguage("ja").setRegion("JP").build()
        LanguageCode.ZH -> Locale.Builder().setLanguage("zh").setRegion("CN").build()
        LanguageCode.ES -> Locale.Builder().setLanguage("es").setRegion("ES").build()
        LanguageCode.IT -> Locale.Builder().setLanguage("it").setRegion("IT").build()
        LanguageCode.FR -> Locale.Builder().setLanguage("fr").setRegion("FR").build()
        LanguageCode.VI -> Locale.Builder().setLanguage("vi").setRegion("VN").build()
        LanguageCode.TH -> Locale.Builder().setLanguage("th").setRegion("TH").build()
    }
}