package com.glootie.meeseeks.domain.usecase.character

import com.glootie.meeseeks.base.BaseUnitTest
import com.glootie.meeseeks.common.DataMock
import com.glootie.meeseeks.core.DataResponse
import com.glootie.meeseeks.data.repository.CharacterRepository
import com.glootie.meeseeks.domain.model.CharacterStatus
import com.glootie.meeseeks.ui.common.UiState
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetCharacterDetailsUseCaseTest : BaseUnitTest() {

    @MockK
    private lateinit var repository: CharacterRepository
    private lateinit var useCase: GetCharacterDetailsUseCase

    @Before
    override fun setUp() {
        super.setUp()
        useCase = GetCharacterDetailsUseCase(repository)
    }

    @Test
    fun `GIVEN repository success WHEN invoke THEN returns UiState Success`() = runTest {
        val details = DataMock.sampleCharacterDetails
        coEvery { repository.getCharacterDetails(1) } returns DataResponse.Success(details)

        val result = useCase(1)

        assertTrue(result is UiState.Success)
        val data = (result as UiState.Success).data
        assertEquals(details.name, data.name)
        assertEquals(CharacterStatus.ALIVE, data.status)
        assertEquals(details.species, data.species)
    }

    @Test
    fun `GIVEN repository error WHEN invoke THEN returns UiState Error`() = runTest {
        val errorMessage = "Not found"
        coEvery { repository.getCharacterDetails(999) } returns DataResponse.Error(errorMessage)

        val result = useCase(999)

        assertTrue(result is UiState.Error)
        assertEquals(errorMessage, (result as UiState.Error).message)
    }
}
