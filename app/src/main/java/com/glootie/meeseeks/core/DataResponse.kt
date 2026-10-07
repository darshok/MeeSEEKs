package com.glootie.meeseeks.core

import retrofit2.Response

sealed class DataResponse<out T> {
    data class Success<out T>(val data: T) : DataResponse<T>()
    data class Error(val error: String) : DataResponse<Nothing>()
}

fun <T> mapResponseToDataResponse(response: Response<T>): DataResponse<T> {
    return if (response.isSuccessful) {
        val body = response.body()
        body?.let {
            DataResponse.Success(body)
        } ?: DataResponse.Error("Response body is null")
    } else {
        DataResponse.Error(response.errorBody()?.string() ?: response.message())
    }
}
