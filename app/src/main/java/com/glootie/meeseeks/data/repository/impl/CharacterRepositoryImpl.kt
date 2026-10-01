package com.glootie.meeseeks.data.repository.impl

import com.glootie.meeseeks.core.DataResponse
import com.glootie.meeseeks.core.mapResponseToDataResponse
import com.glootie.meeseeks.data.entity.response.CharacterDetailsResponse
import com.glootie.meeseeks.data.entity.response.CharacterPaginatedResponse
import com.glootie.meeseeks.data.remote.ApiService
import com.glootie.meeseeks.data.repository.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(val apiService: ApiService) :
    CharacterRepository {

    override suspend fun getCharacters(page: Int): DataResponse<CharacterPaginatedResponse> {
        return withContext(Dispatchers.IO) {
            mapResponseToDataResponse(apiService.getCharacters(page))
        }
    }

    override suspend fun getCharacterDetails(id: Int): DataResponse<CharacterDetailsResponse> {
        return withContext(Dispatchers.IO) {
            mapResponseToDataResponse(apiService.getCharacterDetails(id))
        }
    }
}