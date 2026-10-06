package com.glootie.meeseeks.ui.screen.characterlist

import androidx.paging.PagingData
import com.glootie.meeseeks.base.BaseViewModelTest
import com.glootie.meeseeks.common.DataMock
import com.glootie.meeseeks.domain.usecase.character.GetCharactersUseCase
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class CharacterListViewModelTest : BaseViewModelTest() {

    @MockK
    private lateinit var getCharactersUseCase: GetCharactersUseCase
    private lateinit var viewModel: CharacterListViewModel

    @Before
    override fun setUp() {
        super.setUp()
        every { getCharactersUseCase() } returns flowOf(PagingData.from(listOf(DataMock.sampleCharacterSummary)))
        viewModel = CharacterListViewModel(getCharactersUseCase)
    }

    @Test
    fun `GIVEN viewmodel initialized WHEN created THEN invokes GetCharactersUseCase`() = runTest {
        assertNotNull(viewModel.characterList)
        verify(exactly = 1) { getCharactersUseCase() }
    }
}
