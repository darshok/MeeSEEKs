package com.glootie.meeseeks.ui.screen.characterdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glootie.meeseeks.domain.model.CharacterDetails
import com.glootie.meeseeks.domain.usecase.character.GetCharacterDetailsUseCase
import com.glootie.meeseeks.ui.common.UiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = CharacterDetailsViewModel.Factory::class)
class CharacterDetailsViewModel @AssistedInject constructor(
    @Assisted private val characterId: Int,
    private val getCharacterDetailsUseCase: GetCharacterDetailsUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(characterId: Int): CharacterDetailsViewModel
    }

    private val _characterDetailsUiState = MutableStateFlow<UiState<CharacterDetails>>(UiState.Loading)
    val characterDetailsUiState: StateFlow<UiState<CharacterDetails>> = _characterDetailsUiState.asStateFlow()

    init {
        getCharacterDetails()
    }

    internal fun getCharacterDetails() {
        viewModelScope.launch {
            _characterDetailsUiState.update { getCharacterDetailsUseCase(characterId) }
        }
    }

}