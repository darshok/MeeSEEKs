package com.glootie.meeseeks.data.repository

import androidx.paging.PagingSource
import com.glootie.meeseeks.base.BaseUnitTest
import com.glootie.meeseeks.common.DataMock
import com.glootie.meeseeks.core.DataResponse
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class CharacterSummaryPagingSourceTest : BaseUnitTest() {

    @MockK
    private lateinit var repository: CharacterRepository
    private lateinit var pagingSource: CharacterSummaryPagingSource

    @Before
    override fun setUp() {
        super.setUp()
        pagingSource = CharacterSummaryPagingSource(repository)
    }

    @Test
    fun `GIVEN successful repository response WHEN load page 1 THEN returns LoadResult Page with correct data and keys`() = runTest {
        val paginatedResponse = DataMock.samplePaginatedResponse
        coEvery { repository.getCharacters(1) } returns DataResponse.Success(paginatedResponse)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(1, page.data.size)
        assertEquals(DataMock.sampleCharacterSummaryResponse.name, page.data[0].name)
        assertEquals(null, page.prevKey)
        assertEquals(2, page.nextKey)
    }

    @Test
    fun `GIVEN error repository response WHEN load page 1 THEN returns LoadResult Error`() = runTest {
        val errorMessage = "API Error"
        coEvery { repository.getCharacters(1) } returns DataResponse.Error(errorMessage)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Error)
        val error = (result as PagingSource.LoadResult.Error).throwable
        assertEquals(errorMessage, error.message)
    }

    @Test
    fun `GIVEN IOException WHEN load page 1 THEN returns LoadResult Error`() = runTest {
        val ioException = IOException("Network failure")
        coEvery { repository.getCharacters(1) } throws ioException

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Error)
        val error = (result as PagingSource.LoadResult.Error).throwable
        assertEquals(ioException, error)
    }

    @Test
    fun `GIVEN paging state WHEN getRefreshKey THEN returns anchor position`() {
        val state = androidx.paging.PagingState<Int, com.glootie.meeseeks.domain.model.CharacterSummary>(
            pages = listOf(),
            anchorPosition = 10,
            config = androidx.paging.PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        val refreshKey = pagingSource.getRefreshKey(state)
        assertEquals(10, refreshKey)
    }
}
