package com.glootie.meeseeks.data.repository

import com.glootie.meeseeks.core.DataResponse
import com.glootie.meeseeks.data.entity.response.LocationDetailsResponse

interface LocationRepository {

    suspend fun getLocationDetails(
        id: Int
    ): DataResponse<LocationDetailsResponse>
}