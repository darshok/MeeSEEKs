package com.glootie.meeseeks.data.repository

import com.glootie.meeseeks.core.DataResponse
import com.glootie.meeseeks.data.entity.response.CharacterDetailsResponse
import com.glootie.meeseeks.data.entity.response.CharacterPaginatedResponse
import kotlinx.coroutines.flow.SharedFlow

interface CharacterRepository {

    val isFirstPageLoaded: SharedFlow<Unit>

    suspend fun getCharacters(
        page: Int,
    ): DataResponse<CharacterPaginatedResponse>

    suspend fun getCharacterDetails(
        id: Int
    ): DataResponse<CharacterDetailsResponse>
}