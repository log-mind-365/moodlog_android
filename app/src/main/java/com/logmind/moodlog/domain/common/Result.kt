package com.logmind.moodlog.domain.common

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()

    fun isSuccess(): Boolean = this is Success
    fun isError(): Boolean = this is Error

    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Error -> null
    }

    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error -> throw exception
    }

    inline fun onSuccess(action: (value: T) -> Unit): Result<T> {
        if (this is Success) {
            action(data)
        }
        return this
    }

    inline fun onError(action: (exception: Throwable) -> Unit): Result<T> {
        if (this is Error) {
            action(exception)
        }
        return this
    }

    companion object {
        fun <T> success(data: T): Result<T> = Success(data)
        fun error(exception: Throwable): Result<Nothing> = Error(exception)
        fun <T> runCatching(block: () -> T): Result<T> {
            return try {
                success(block())
            } catch (e: Throwable) {
                error(e)
            }
        }
    }
}