package com.glootie.meeseeks.data.repository.impl

import com.glootie.meeseeks.core.DataResponse
import com.glootie.meeseeks.core.safeApiCall
import com.glootie.meeseeks.data.entity.response.CharacterDetailsResponse
import com.glootie.meeseeks.data.entity.response.CharacterPaginatedResponse
import com.glootie.meeseeks.data.remote.ApiService
import com.glootie.meeseeks.data.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(val apiService: ApiService) :
    CharacterRepository {

    private val _isFirstPageLoaded = MutableSharedFlow<Unit>()
    override val isFirstPageLoaded: SharedFlow<Unit> = _isFirstPageLoaded.asSharedFlow()

    override suspend fun getCharacters(page: Int): DataResponse<CharacterPaginatedResponse> {
        return safeApiCall {
            apiService.getCharacters(page)
        }.also {
            if (page == 1) {
                _isFirstPageLoaded.emit(Unit)
            }
        }
    }

    override suspend fun getCharacterDetails(id: Int): DataResponse<CharacterDetailsResponse> {
        return safeApiCall {
            apiService.getCharacterDetails(id)
        }
    }
}
