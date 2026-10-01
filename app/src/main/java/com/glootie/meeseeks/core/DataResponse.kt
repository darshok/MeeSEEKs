package com.glootie.meeseeks.core

import retrofit2.Response

sealed class DataResponse<out T> {
    class Success<out T>(val data: T) : DataResponse<T>()
    class Error(val error: String) : DataResponse<Nothing>()
}

fun <T> mapResponseToDataResponse(response: Response<T>) : DataResponse<T> {
    return when {
        response.code() == 200 -> DataResponse.Success(response.body() as T)
        else -> DataResponse.Error(response.message())
    }
}