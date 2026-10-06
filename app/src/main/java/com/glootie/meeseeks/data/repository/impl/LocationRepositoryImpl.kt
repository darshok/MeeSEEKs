package com.glootie.meeseeks.data.repository.impl

import com.glootie.meeseeks.core.DataResponse
import com.glootie.meeseeks.core.safeApiCall
import com.glootie.meeseeks.data.entity.response.LocationDetailsResponse
import com.glootie.meeseeks.data.remote.ApiService
import com.glootie.meeseeks.data.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(val apiService: ApiService) : LocationRepository {

    override suspend fun getLocationDetails(id: Int): DataResponse<LocationDetailsResponse> {
        return safeApiCall {
            apiService.getLocationDetails(id)
        }
    }
}