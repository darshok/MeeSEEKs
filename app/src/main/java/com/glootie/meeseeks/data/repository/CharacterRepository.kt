package com.glootie.meeseeks.data.repository

import androidx.paging.PagingData
import com.glootie.meeseeks.core.DataResponse
import com.glootie.meeseeks.data.entity.response.CharacterDetailsResponse
import com.glootie.meeseeks.data.entity.response.CharacterPaginatedResponse
import com.glootie.meeseeks.domain.model.CharacterSummary
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    suspend fun getCharacters(
        page: Int,
    ): DataResponse<CharacterPaginatedResponse>

    suspend fun getCharacterDetails(
        id: Int
    ): DataResponse<CharacterDetailsResponse>
}