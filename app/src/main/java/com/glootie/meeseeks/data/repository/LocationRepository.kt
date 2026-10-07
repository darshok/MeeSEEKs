package com.glootie.meeseeks.data.repository

import com.glootie.meeseeks.core.DataResponse
import com.glootie.meeseeks.domain.model.LocationDetails

interface LocationRepository {

    suspend fun getLocationDetails(
        id: Int
    ): DataResponse<LocationDetails>
}
