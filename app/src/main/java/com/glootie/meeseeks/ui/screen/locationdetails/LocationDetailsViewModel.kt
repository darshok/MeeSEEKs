package com.glootie.meeseeks.ui.screen.locationdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glootie.meeseeks.domain.model.LocationDetails
import com.glootie.meeseeks.domain.usecase.location.GetLocationDetailsUseCase
import com.glootie.meeseeks.ui.common.UiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = LocationDetailsViewModel.Factory::class)
class LocationDetailsViewModel @AssistedInject constructor(
    @Assisted private val locationId: Int,
    private val getLocationDetailsUseCase: GetLocationDetailsUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(characterId: Int): LocationDetailsViewModel
    }

    private val _locationDetailsUiState = MutableStateFlow<UiState<LocationDetails>>(UiState.Loading)
    val locationDetailsUiState: StateFlow<UiState<LocationDetails>> = _locationDetailsUiState.asStateFlow()

    init {
        getLocationDetails()
    }

    internal fun getLocationDetails() {
        _locationDetailsUiState.update { UiState.Loading }
        viewModelScope.launch {
            _locationDetailsUiState.update { getLocationDetailsUseCase(locationId) }
        }
    }
}