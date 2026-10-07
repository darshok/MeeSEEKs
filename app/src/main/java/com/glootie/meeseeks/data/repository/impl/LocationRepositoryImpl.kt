package com.glootie.meeseeks.data.repository.impl

import com.glootie.meeseeks.core.DataResponse
import com.glootie.meeseeks.core.safeApiCall
import com.glootie.meeseeks.data.local.dao.LocationDao
import com.glootie.meeseeks.data.local.entity.LocationEntity
import com.glootie.meeseeks.data.local.entity.toLocationDetails
import com.glootie.meeseeks.data.remote.ApiService
import com.glootie.meeseeks.data.repository.LocationRepository
import com.glootie.meeseeks.domain.model.LocationDetails
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val locationDao: LocationDao
) : LocationRepository {

    override suspend fun getLocationDetails(id: Int): DataResponse<LocationDetails> {
        val cachedLocation = locationDao.getLocationById(id)
        return cachedLocation?.let {
            DataResponse.Success(cachedLocation.toLocationDetails())
        } ?: when (val apiResponse = safeApiCall { apiService.getLocationDetails(id) }) {
            is DataResponse.Success -> {
                val detailResponse = apiResponse.data
                val entity = LocationEntity(
                    id = id,
                    name = detailResponse.name,
                    type = detailResponse.type,
                    dimension = detailResponse.dimension
                )
                locationDao.insert(entity)
                DataResponse.Success(entity.toLocationDetails())
            }

            is DataResponse.Error -> DataResponse.Error(apiResponse.error)
        }
    }
}
