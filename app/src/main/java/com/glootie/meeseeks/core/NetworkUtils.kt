package com.glootie.meeseeks.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> Response<T>
): DataResponse<T> {
    return withContext(dispatcher) {
        try {
            mapResponseToDataResponse(apiCall())
        } catch (e: Exception) {
            DataResponse.Error(e.localizedMessage ?: "Network error")
        }
    }
}
