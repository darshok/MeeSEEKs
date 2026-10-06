package com.glootie.meeseeks.data.repository

import com.glootie.meeseeks.base.BaseUnitTest
import com.glootie.meeseeks.common.DataMock
import com.glootie.meeseeks.core.DataResponse
import com.glootie.meeseeks.data.remote.ApiService
import com.glootie.meeseeks.data.repository.impl.CharacterRepositoryImpl
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CharacterRepositoryImplTest : BaseUnitTest() {

    @MockK
    private lateinit var apiService: ApiService
    private lateinit var repository: CharacterRepositoryImpl

    @Before
    override fun setUp() {
        super.setUp()
        repository = CharacterRepositoryImpl(apiService)
    }

    @Test
    fun `GIVEN first page response WHEN get characters THEN updates is first page loaded`() = runTest {
        val mockResponse = DataMock.samplePaginatedResponse
        coEvery { apiService.getCharacters(1) } returns Response.success(mockResponse)

        assertFalse(repository.isFirstPageLoaded.first())

        val result = repository.getCharacters(1)

        assertTrue(result is DataResponse.Success)
        assertEquals(mockResponse, (result as DataResponse.Success).data)
        assertTrue(repository.isFirstPageLoaded.first())
    }

    @Test
    fun `GIVEN successful API response WHEN get character details THEN returns data response success`() = runTest {
        val mockDetails = DataMock.sampleCharacterDetailsResponse
        coEvery { apiService.getCharacterDetails(1) } returns Response.success(mockDetails)

        val result = repository.getCharacterDetails(1)

        assertTrue(result is DataResponse.Success)
        assertEquals(mockDetails, (result as DataResponse.Success).data)
    }

    @Test
    fun `GIVEN network exception WHEN get character details THEN returns data response error`() = runTest {
        coEvery { apiService.getCharacterDetails(999) } throws RuntimeException("Network error")

        val result = repository.getCharacterDetails(999)

        assertTrue(result is DataResponse.Error)
    }
}
