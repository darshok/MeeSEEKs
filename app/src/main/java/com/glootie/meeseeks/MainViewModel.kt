package com.glootie.meeseeks

import androidx.lifecycle.ViewModel
import com.glootie.meeseeks.data.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    characterRepository: CharacterRepository
) : ViewModel() {

    val isFirstPageLoaded: StateFlow<Boolean> = characterRepository.isFirstPageLoaded
}