package com.glootie.meeseeks.domain.usecase.character

import com.glootie.meeseeks.core.DataResponse
import com.glootie.meeseeks.data.entity.response.mapToDomain
import com.glootie.meeseeks.data.repository.CharacterRepository
import com.glootie.meeseeks.domain.model.CharacterDetails
import com.glootie.meeseeks.ui.common.UiState
import javax.inject.Inject

class GetCharacterDetailsUseCase @Inject constructor(private val repository: CharacterRepository) {
    suspend operator fun invoke(id: Int): UiState<CharacterDetails> {
        return when (val characterDetails = repository.getCharacterDetails(id)) {
            is DataResponse.Error -> UiState.Error(characterDetails.error)
            is DataResponse.Success -> UiState.Success(characterDetails.data.mapToDomain())
        }
    }
}