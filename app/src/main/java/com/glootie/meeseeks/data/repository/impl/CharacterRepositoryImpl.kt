package com.glootie.meeseeks.data.repository.impl

import com.glootie.meeseeks.core.DataResponse
import com.glootie.meeseeks.core.safeApiCall
import com.glootie.meeseeks.data.entity.response.CharacterDetailsResponse
import com.glootie.meeseeks.data.entity.response.CharacterPaginatedResponse
import com.glootie.meeseeks.data.remote.ApiService
import com.glootie.meeseeks.data.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(val apiService: ApiService) :
    CharacterRepository {

    private val _isFirstPageLoaded = MutableStateFlow(false)
    override val isFirstPageLoaded: StateFlow<Boolean> = _isFirstPageLoaded.asStateFlow()

    override suspend fun getCharacters(page: Int): DataResponse<CharacterPaginatedResponse> {
        return safeApiCall {
            apiService.getCharacters(page)
        }.also {
            if (page == 1) {
                _isFirstPageLoaded.update { true }
            }
        }
    }

    override suspend fun getCharacterDetails(id: Int): DataResponse<CharacterDetailsResponse> {
        return safeApiCall {
            apiService.getCharacterDetails(id)
        }
    }
}
