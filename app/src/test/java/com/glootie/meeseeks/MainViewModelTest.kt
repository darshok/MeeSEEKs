package com.glootie.meeseeks

import com.glootie.meeseeks.base.BaseViewModelTest
import com.glootie.meeseeks.data.repository.CharacterRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MainViewModelTest : BaseViewModelTest() {

    @MockK
    private lateinit var characterRepository: CharacterRepository

    private lateinit var viewModel: MainViewModel
    private val isFirstPageLoadedFlow = MutableStateFlow(false)

    @Before
    override fun setUp() {
        super.setUp()
        every { characterRepository.isFirstPageLoaded } returns isFirstPageLoadedFlow
        viewModel = MainViewModel(characterRepository)
    }

    @Test
    fun `GIVEN characterRepository WHEN isFirstPageLoaded emits THEN viewModel matches state`() {
        assertEquals(false, viewModel.isFirstPageLoaded.value)

        isFirstPageLoadedFlow.value = true

        assertEquals(true, viewModel.isFirstPageLoaded.value)
    }
}
