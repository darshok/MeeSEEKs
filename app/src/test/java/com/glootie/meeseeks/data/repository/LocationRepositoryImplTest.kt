package com.glootie.meeseeks.data.repository

import com.glootie.meeseeks.base.BaseUnitTest
import com.glootie.meeseeks.common.DataMock
import com.glootie.meeseeks.core.DataResponse
import com.glootie.meeseeks.data.remote.ApiService
import com.glootie.meeseeks.data.repository.impl.LocationRepositoryImpl
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class LocationRepositoryImplTest : BaseUnitTest() {

    @MockK
    private lateinit var apiService: ApiService
    private lateinit var repository: LocationRepositoryImpl

    @Before
    override fun setUp() {
        super.setUp()
        repository = LocationRepositoryImpl(apiService)
    }

    @Test
    fun `GIVEN successful API response WHEN getLocationDetails THEN returns DataResponse Success`() = runTest {
        val mockResponse = DataMock.sampleLocationDetailsResponse
        coEvery { apiService.getLocationDetails(1) } returns Response.success(mockResponse)

        val result = repository.getLocationDetails(1)

        assertTrue(result is DataResponse.Success)
        assertEquals(mockResponse, (result as DataResponse.Success).data)
    }

    @Test
    fun `GIVEN network exception WHEN getLocationDetails THEN returns DataResponse Error`() = runTest {
        coEvery { apiService.getLocationDetails(999) } throws RuntimeException("Network error")

        val result = repository.getLocationDetails(999)

        assertTrue(result is DataResponse.Error)
    }
}
