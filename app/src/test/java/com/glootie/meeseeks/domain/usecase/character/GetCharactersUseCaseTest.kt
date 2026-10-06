package com.glootie.meeseeks.domain.usecase.character

import androidx.paging.testing.asSnapshot
import com.glootie.meeseeks.base.BaseUnitTest
import com.glootie.meeseeks.common.DataMock
import com.glootie.meeseeks.core.DataResponse
import com.glootie.meeseeks.data.entity.response.CharacterPaginatedResponse
import com.glootie.meeseeks.data.entity.response.PageInfoResponse
import com.glootie.meeseeks.data.repository.CharacterRepository
import com.glootie.meeseeks.domain.model.CharacterStatus
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetCharactersUseCaseTest : BaseUnitTest() {

    @MockK
    private lateinit var repository: CharacterRepository
    private lateinit var useCase: GetCharactersUseCase

    @Before
    override fun setUp() {
        super.setUp()
        useCase = GetCharactersUseCase(repository)
    }

    @Test
    fun `GIVEN repository returns paginated characters WHEN invoke THEN emits mapped character summaries`() = runTest {
        val page1Response = CharacterPaginatedResponse(
            info = PageInfoResponse(
                count = 1,
                pages = 1,
                next = "",
                prev = null
            ),
            results = listOf(DataMock.sampleCharacterSummaryResponse)
        )
        coEvery { repository.getCharacters(1) } returns DataResponse.Success(page1Response)

        val items = useCase().asSnapshot()

        assertEquals(1, items.size)
        assertEquals(DataMock.sampleCharacterSummaryResponse.id, items[0].id)
        assertEquals(DataMock.sampleCharacterSummaryResponse.name, items[0].name)
        assertEquals(CharacterStatus.ALIVE, items[0].status)
    }
}
