package com.glootie.meeseeks.data.repository

import com.glootie.meeseeks.base.BaseUnitTest
import com.glootie.meeseeks.common.DataMock
import com.glootie.meeseeks.core.DataResponse
import com.glootie.meeseeks.data.local.MeeseeksDatabase
import com.glootie.meeseeks.data.local.dao.CharacterDao
import com.glootie.meeseeks.data.local.entity.CharacterEntity
import com.glootie.meeseeks.data.remote.ApiService
import com.glootie.meeseeks.data.repository.impl.CharacterRepositoryImpl
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CharacterRepositoryImplTest : BaseUnitTest() {

    @MockK
    private lateinit var apiService: ApiService

    @MockK
    private lateinit var database: MeeseeksDatabase

    @MockK
    private lateinit var characterDao: CharacterDao

    private lateinit var repository: CharacterRepositoryImpl

    @Before
    override fun setUp() {
        super.setUp()
        every { database.characterDao() } returns characterDao
        repository = CharacterRepositoryImpl(apiService, database)
    }

    @Test
    fun `GIVEN cached detail WHEN get character details THEN returns data response success from cache`() =
        runTest {
            val cachedEntity = CharacterEntity(
                id = 1,
                name = "Rick Sanchez",
                status = "ALIVE",
                image = "url",
                species = "Human",
                gender = "Male",
                originLocationId = 1,
                originLocationName = "Earth",
                lastLocationId = 1,
                lastLocationName = "Earth"
            )
            coEvery { characterDao.getCharacterById(1) } returns cachedEntity

            val result = repository.getCharacterDetails(1)

            assertTrue(result is DataResponse.Success)
            assertEquals(cachedEntity.name, (result as DataResponse.Success).data.name)
        }

    @Test
    fun `GIVEN no cache and successful API response WHEN get character details THEN returns data response success and updates entity`() =
        runTest {
            val mockDetails = DataMock.sampleCharacterDetailsResponse
            val listEntity =
                CharacterEntity(id = 1, name = "Summer Smith", status = "ALIVE", image = "url")

            coEvery { characterDao.getCharacterById(1) } returns listEntity
            coEvery { apiService.getCharacterDetails(1) } returns Response.success(mockDetails)
            coEvery { characterDao.insert(any()) } returns 1L

            val result = repository.getCharacterDetails(1)

            assertTrue(result is DataResponse.Success)
            assertEquals(mockDetails.name, (result as DataResponse.Success).data.name)
        }

    @Test
    fun `GIVEN network exception WHEN get character details THEN returns data response error`() =
        runTest {
            coEvery { characterDao.getCharacterById(999) } returns null
            coEvery { apiService.getCharacterDetails(999) } throws RuntimeException("Network error")

            val result = repository.getCharacterDetails(999)

            assertTrue(result is DataResponse.Error)
        }
}
