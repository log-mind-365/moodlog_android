package com.logmind.moodlog.utils

import android.util.Log

inline fun <T> execute(operation: String, block: () -> T): T {
    return runCatching {
        block()
    }.onFailure {
        Log.e("Operation: $operation", "${it.message}", it)
    }.getOrThrow()
}