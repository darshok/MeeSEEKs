package com.glootie.meeseeks.data.repository

import androidx.paging.PagingData
import com.glootie.meeseeks.core.DataResponse
import com.glootie.meeseeks.domain.model.CharacterDetails
import com.glootie.meeseeks.domain.model.CharacterSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface CharacterRepository {

    val isFirstPageLoaded: StateFlow<Boolean>

    fun getCharacters(query: String): Flow<PagingData<CharacterSummary>>

    suspend fun getCharacterDetails(
        id: Int
    ): DataResponse<CharacterDetails>
}
