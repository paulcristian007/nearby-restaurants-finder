package com.example.restaurants.core

import android.util.Log

sealed interface Result<out T> {
    object Start: Result<Nothing>
    data class Error(val exception: Throwable? = null) : Result<Nothing>
    data class Success<T>(val data: T): Result<T>
    object Loading : Result<Nothing>
}