package com.glootie.meeseeks.domain.usecase.character

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.glootie.meeseeks.base.BaseUnitTest
import com.glootie.meeseeks.common.DataMock
import com.glootie.meeseeks.data.repository.CharacterRepository
import com.glootie.meeseeks.domain.model.CharacterStatus
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
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
    fun `GIVEN repository returns paginated characters WHEN invoke THEN emits mapped character summaries`() =
        runTest {
            val pagingData = PagingData.from(listOf(DataMock.sampleCharacterSummary))
            every { repository.getCharacters(any()) } returns flowOf(pagingData)

            val items = useCase().asSnapshot()

            assertEquals(1, items.size)
            assertEquals(DataMock.sampleCharacterSummary.id, items[0].id)
            assertEquals(DataMock.sampleCharacterSummary.name, items[0].name)
            assertEquals(CharacterStatus.ALIVE, items[0].status)
        }
}
