package com.example.healthhelper.core

sealed class ResultOfRequest<out T> {
    data class Success<out T>(val result: T) : ResultOfRequest<T>()
    data class Error(val errorMessage: String) : ResultOfRequest<Nothing>()
    data object Loading : ResultOfRequest<Nothing>()
}