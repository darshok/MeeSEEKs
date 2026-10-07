package com.glootie.meeseeks.data.repository

import com.glootie.meeseeks.base.BaseUnitTest
import com.glootie.meeseeks.common.DataMock
import com.glootie.meeseeks.core.DataResponse
import com.glootie.meeseeks.data.local.MeeseeksDatabase
import com.glootie.meeseeks.data.local.dao.LocationDao
import com.glootie.meeseeks.data.local.entity.LocationEntity
import com.glootie.meeseeks.data.remote.ApiService
import com.glootie.meeseeks.data.repository.impl.LocationRepositoryImpl
import io.mockk.coEvery
import io.mockk.every
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

    @MockK
    private lateinit var database: MeeseeksDatabase

    @MockK
    private lateinit var locationDao: LocationDao

    private lateinit var repository: LocationRepositoryImpl

    @Before
    override fun setUp() {
        super.setUp()
        every { database.locationDao() } returns locationDao
        repository = LocationRepositoryImpl(apiService, database)
    }

    @Test
    fun `GIVEN cached location WHEN getLocationDetails THEN returns DataResponse Success from cache`() =
        runTest {
            val cachedEntity = LocationEntity(1, "Earth (C-137)", "Planet", "Dimension C-137")
            coEvery { locationDao.getLocationById(1) } returns cachedEntity

            val result = repository.getLocationDetails(1)

            assertTrue(result is DataResponse.Success)
            assertEquals("Earth (C-137)", (result as DataResponse.Success).data.name)
        }

    @Test
    fun `GIVEN no cache and successful API response WHEN getLocationDetails THEN returns DataResponse Success and caches`() =
        runTest {
            val mockResponse = DataMock.sampleLocationDetailsResponse
            coEvery { locationDao.getLocationById(1) } returns null
            coEvery { apiService.getLocationDetails(1) } returns Response.success(mockResponse)
            coEvery { locationDao.insert(any()) } returns 1L

            val result = repository.getLocationDetails(1)

            assertTrue(result is DataResponse.Success)
            assertEquals(mockResponse.name, (result as DataResponse.Success).data.name)
        }

    @Test
    fun `GIVEN no cache and network exception WHEN getLocationDetails THEN returns DataResponse Error`() =
        runTest {
            coEvery { locationDao.getLocationById(999) } returns null
            coEvery { apiService.getLocationDetails(999) } throws RuntimeException("Network error")

            val result = repository.getLocationDetails(999)

            assertTrue(result is DataResponse.Error)
        }
}
