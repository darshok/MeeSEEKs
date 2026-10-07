package com.glootie.meeseeks.ui.screen.characterlist

import androidx.paging.PagingData
import com.glootie.meeseeks.base.BaseViewModelTest
import com.glootie.meeseeks.common.DataMock
import com.glootie.meeseeks.domain.usecase.character.GetCharactersUseCase
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterListViewModelTest : BaseViewModelTest() {

    @MockK
    private lateinit var getCharactersUseCase: GetCharactersUseCase
    private lateinit var viewModel: CharacterListViewModel

    @Before
    override fun setUp() {
        super.setUp()
        every { getCharactersUseCase(any()) } returns flowOf(PagingData.from(listOf(DataMock.sampleCharacterSummary)))
        viewModel = CharacterListViewModel(getCharactersUseCase)
    }

    @Test
    fun `GIVEN viewmodel initialized WHEN created THEN invokes GetCharactersUseCase`() = runTest {
        assertNotNull(viewModel.characterList)

        advanceTimeBy(400.milliseconds)

        viewModel.characterList.first()

        verify(exactly = 1) { getCharactersUseCase("") }
    }

    @Test
    fun `GIVEN a new search query WHEN updateSearchQuery is called THEN invokes GetCharactersUseCase with query`() =
        runTest {
            advanceTimeBy(400.milliseconds)
            viewModel.characterList.first()

            viewModel.updateSearchQuery("Rick")

            advanceTimeBy(400.milliseconds)
            viewModel.characterList.first()

            verify(exactly = 1) { getCharactersUseCase("Rick") }
        }
}