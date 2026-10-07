package com.glootie.meeseeks.data.repository.impl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.glootie.meeseeks.core.DataResponse
import com.glootie.meeseeks.core.safeApiCall
import com.glootie.meeseeks.data.local.MeeseeksDatabase
import com.glootie.meeseeks.data.local.dao.CharacterDao
import com.glootie.meeseeks.data.local.entity.CharacterEntity
import com.glootie.meeseeks.data.local.entity.toCharacterDetails
import com.glootie.meeseeks.data.local.entity.toCharacterSummary
import com.glootie.meeseeks.data.remote.ApiService
import com.glootie.meeseeks.data.repository.CharacterRemoteMediator
import com.glootie.meeseeks.data.repository.CharacterRepository
import com.glootie.meeseeks.domain.model.CharacterDetails
import com.glootie.meeseeks.domain.model.CharacterSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject


const val PAGE_SIZE = 20
const val INITIAL_LOAD_SIZE = 40
const val PREFETCH_DISTANCE = 5
class CharacterRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val characterDao: CharacterDao,
    private val database: MeeseeksDatabase
) : CharacterRepository {

    private val _isFirstPageLoaded = MutableStateFlow(false)
    override val isFirstPageLoaded: StateFlow<Boolean> = _isFirstPageLoaded.asStateFlow()

    @OptIn(ExperimentalPagingApi::class)
    override fun getCharacters(query: String): Flow<PagingData<CharacterSummary>> {
        val pagingSourceFactory = { characterDao.pagingSource(query) }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = INITIAL_LOAD_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = false
            ),
            remoteMediator = CharacterRemoteMediator(
                query = query,
                database = database,
                apiService = apiService
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { it.toCharacterSummary() }
        }.also {
            _isFirstPageLoaded.update { true }
        }
    }

    override suspend fun getCharacterDetails(id: Int): DataResponse<CharacterDetails> {
        val cachedDetail = characterDao.getCharacterById(id)?.toCharacterDetails()
        if (cachedDetail != null) {
            return DataResponse.Success(cachedDetail)
        }

        return when (val apiResponse = safeApiCall { apiService.getCharacterDetails(id) }) {
            is DataResponse.Success -> {
                with(apiResponse.data) {
                    val entity = CharacterEntity(
                        id = id,
                        name = name,
                        status = status,
                        image = image,
                        species = species,
                        gender = gender,
                        originLocationId = originLocation.url.substringAfterLast("/")
                            .toIntOrNull() ?: 0,
                        originLocationName = originLocation.name,
                        lastLocationId = lastLocation.url.substringAfterLast("/")
                            .toIntOrNull() ?: 0,
                        lastLocationName = lastLocation.name
                    )

                    characterDao.insert(entity)
                    DataResponse.Success(entity.toCharacterDetails()!!)
                }
            }

            is DataResponse.Error -> DataResponse.Error(apiResponse.error)
        }
    }
}
