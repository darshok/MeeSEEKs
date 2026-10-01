package com.glootie.meeseeks.ui.common

import androidx.compose.runtime.Immutable

@Immutable
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    
    data class Success<T>(val data: T) : UiState<T>
    
    data class Error(
        val message: String? = null,
        val throwable: Throwable? = null
    ) : UiState<Nothing>
}

fun <T> UiState<T>.getOrNull(): T? = (this as? UiState.Success)?.data

fun <T> UiState<T>.isLoading(): Boolean = this is UiState.Loading

fun <T> UiState<T>.isError(): Boolean = this is UiState.Error

fun <T, R> UiState<T>.map(transform: (T) -> R): UiState<R> = when (this) {
    is UiState.Loading -> UiState.Loading
    is UiState.Success -> UiState.Success(transform(data))
    is UiState.Error -> UiState.Error(message, throwable)
}
