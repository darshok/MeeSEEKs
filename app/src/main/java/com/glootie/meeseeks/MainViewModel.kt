package com.glootie.meeseeks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glootie.meeseeks.data.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _isFirstPageLoaded = MutableSharedFlow<Unit>()
    val isFirstPageLoaded: SharedFlow<Unit> = _isFirstPageLoaded.asSharedFlow()

    init {
        getIsFirstPageLoaded()
    }

    private fun getIsFirstPageLoaded() {
        viewModelScope.launch {
            characterRepository.isFirstPageLoaded.collect {
                _isFirstPageLoaded.emit(Unit)
            }
        }
    }
}