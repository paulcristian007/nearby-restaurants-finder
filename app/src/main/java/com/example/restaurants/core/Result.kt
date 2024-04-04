package com.example.restaurants.core
import android.util.Log

sealed interface Result<out T> {
    object Start: Result<Nothing>
    data class Success<T>(val data: T) : Result<T>
    object Distances : Result<Nothing>
    data class Error(val exception: Throwable? = null) : Result<Nothing>
    data class Photo<T>(val data: T): Result<T>
    data class Sorted<T>(val data: T): Result<T>
    object Loading : Result<Nothing>
}

/*
override fun equals(other: Any?): Boolean {
    when (other) {
        is Result.Loading -> {
            Log.d(TAG, "equals loading")
        }

        is Result.Success<*> -> {
            Log.d(TAG, "equals success")
        }
        is Result.Error -> {
            Log.d(TAG, "equals error")
        }
        else -> {
            Log.d(TAG, "equals")
        }
    }
    return false
}

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
 */