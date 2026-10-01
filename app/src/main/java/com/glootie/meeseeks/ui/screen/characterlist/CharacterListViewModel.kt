package com.glootie.meeseeks.ui.screen.characterlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.glootie.meeseeks.domain.model.CharacterSummary
import com.glootie.meeseeks.domain.usecase.character.GetCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    val characterList: Flow<PagingData<CharacterSummary>> =
        getCharactersUseCase().cachedIn(viewModelScope)
}