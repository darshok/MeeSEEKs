package com.glootie.meeseeks.domain.usecase.location

import com.glootie.meeseeks.base.BaseUnitTest
import com.glootie.meeseeks.common.DataMock
import com.glootie.meeseeks.core.DataResponse
import com.glootie.meeseeks.data.repository.LocationRepository
import com.glootie.meeseeks.ui.common.UiState
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetLocationDetailsUseCaseTest : BaseUnitTest() {

    @MockK
    private lateinit var repository: LocationRepository
    private lateinit var useCase: GetLocationDetailsUseCase

    @Before
    override fun setUp() {
        super.setUp()
        useCase = GetLocationDetailsUseCase(repository)
    }

    @Test
    fun `GIVEN repository success WHEN invoke THEN returns UiState Success`() = runTest {
        val details = DataMock.sampleLocationDetails
        coEvery { repository.getLocationDetails(1) } returns DataResponse.Success(details)

        val result = useCase(1)

        assertTrue(result is UiState.Success)
        val data = (result as UiState.Success).data
        assertEquals(details.name, data.name)
        assertEquals(details.type, data.type)
        assertEquals(details.dimension, data.dimension)
    }

    @Test
    fun `GIVEN repository error WHEN invoke THEN returns UiState Error`() = runTest {
        val errorMessage = "Not found"
        coEvery { repository.getLocationDetails(999) } returns DataResponse.Error(errorMessage)

        val result = useCase(999)

        assertTrue(result is UiState.Error)
        assertEquals(errorMessage, (result as UiState.Error).message)
    }
}
