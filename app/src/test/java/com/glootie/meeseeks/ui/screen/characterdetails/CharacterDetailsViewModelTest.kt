package com.glootie.meeseeks.ui.screen.characterdetails

import com.glootie.meeseeks.base.BaseViewModelTest
import com.glootie.meeseeks.common.DataMock
import com.glootie.meeseeks.domain.usecase.character.GetCharacterDetailsUseCase
import com.glootie.meeseeks.ui.common.UiState
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CharacterDetailsViewModelTest : BaseViewModelTest() {

    @MockK
    private lateinit var getCharacterDetailsUseCase: GetCharacterDetailsUseCase

    @Test
    fun `GIVEN use case success WHEN ViewModel init THEN emits UiState Success`() = runTest {
        val characterDetails = DataMock.sampleCharacterDetails
        coEvery { getCharacterDetailsUseCase(1) } returns UiState.Success(characterDetails)

        val viewModel = CharacterDetailsViewModel(1, getCharacterDetailsUseCase)

        testDispatcher.scheduler.advanceUntilIdle()

        val currentState = viewModel.characterDetailsUiState.value
        assertTrue(currentState is UiState.Success)
        assertEquals(characterDetails, (currentState as UiState.Success).data)
    }

    @Test
    fun `GIVEN use case error WHEN ViewModel init THEN emits UiState Error`() = runTest {
        coEvery { getCharacterDetailsUseCase(2) } returns UiState.Error("Error loading details")

        val viewModel = CharacterDetailsViewModel(2, getCharacterDetailsUseCase)

        testDispatcher.scheduler.advanceUntilIdle()

        val currentState = viewModel.characterDetailsUiState.value
        assertTrue(currentState is UiState.Error)
        assertEquals("Error loading details", (currentState as UiState.Error).message)
    }
}
