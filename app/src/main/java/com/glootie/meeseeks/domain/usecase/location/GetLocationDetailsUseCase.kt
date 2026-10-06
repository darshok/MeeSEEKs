package com.glootie.meeseeks.domain.usecase.location

import com.glootie.meeseeks.core.DataResponse
import com.glootie.meeseeks.data.repository.LocationRepository
import com.glootie.meeseeks.domain.model.LocationDetails
import com.glootie.meeseeks.domain.model.mapToDomain
import com.glootie.meeseeks.ui.common.UiState
import javax.inject.Inject

class GetLocationDetailsUseCase @Inject constructor(val repository: LocationRepository) {
    suspend operator fun invoke(id: Int): UiState<LocationDetails> {
        return when (val locationDetails = repository.getLocationDetails(id)) {
            is DataResponse.Error -> UiState.Error(locationDetails.error)
            is DataResponse.Success -> UiState.Success(locationDetails.data.mapToDomain())
        }
    }
}