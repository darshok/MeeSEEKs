package com.glootie.meeseeks.ui.screen.locationdetails

import com.glootie.meeseeks.base.BaseViewModelTest
import com.glootie.meeseeks.common.DataMock
import com.glootie.meeseeks.domain.usecase.location.GetLocationDetailsUseCase
import com.glootie.meeseeks.ui.common.UiState
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LocationDetailsViewModelTest : BaseViewModelTest() {

    @MockK
    private lateinit var getLocationDetailsUseCase: GetLocationDetailsUseCase

    @Test
    fun `GIVEN use case success WHEN ViewModel init THEN emits UiState Success`() = runTest {
        val locationDetails = DataMock.sampleLocationDetails
        coEvery { getLocationDetailsUseCase(1) } returns UiState.Success(locationDetails)

        val viewModel = LocationDetailsViewModel(1, getLocationDetailsUseCase)

        testDispatcher.scheduler.advanceUntilIdle()

        val currentState = viewModel.locationDetailsUiState.value
        assertTrue(currentState is UiState.Success)
        assertEquals(locationDetails, (currentState as UiState.Success).data)
    }

    @Test
    fun `GIVEN use case error WHEN ViewModel init THEN emits UiState Error`() = runTest {
        coEvery { getLocationDetailsUseCase(2) } returns UiState.Error("Error loading location details")

        val viewModel = LocationDetailsViewModel(2, getLocationDetailsUseCase)

        testDispatcher.scheduler.advanceUntilIdle()

        val currentState = viewModel.locationDetailsUiState.value
        assertTrue(currentState is UiState.Error)
        assertEquals("Error loading location details", (currentState as UiState.Error).message)
    }
}
