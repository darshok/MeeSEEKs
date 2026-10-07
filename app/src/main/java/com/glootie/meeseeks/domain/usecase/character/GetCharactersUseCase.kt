package com.glootie.meeseeks.domain.usecase.character

import androidx.paging.PagingData
import com.glootie.meeseeks.data.repository.CharacterRepository
import com.glootie.meeseeks.domain.model.CharacterSummary
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(val repository: CharacterRepository) {
    operator fun invoke(query: String = ""): Flow<PagingData<CharacterSummary>> {
        return repository.getCharacters(query)
    }
}
