package com.glootie.meeseeks.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.glootie.meeseeks.data.entity.response.toEntity
import com.glootie.meeseeks.data.local.MeeseeksDatabase
import com.glootie.meeseeks.data.local.entity.CharacterEntity
import com.glootie.meeseeks.data.local.entity.RemoteKeyEntity
import com.glootie.meeseeks.data.remote.ApiService
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val query: String,
    private val database: MeeseeksDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, CharacterEntity>() {

    private val characterDao = database.characterDao()
    private val remoteKeyDao = database.remoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        val count = characterDao.getCharacterCount()
        return if (count == 0) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    remoteKeys?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
            }

            val apiResponse = apiService.getCharacters(page, query.takeIf { it.isNotBlank() })
            if (!apiResponse.isSuccessful) {
                return MediatorResult.Error(Exception(apiResponse.message()))
            }

            val paginatedData =
                apiResponse.body() ?: return MediatorResult.Success(endOfPaginationReached = true)

            val characters = paginatedData.results
            val endOfPaginationReached = paginatedData.info.next.isNullOrBlank()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearRemoteKeys()
                    characterDao.clearAll()
                }

                val prevKey = paginatedData.info.prev?.let { getKey(it) }
                val nextKey = paginatedData.info.next?.let { getKey(it) }

                val keys = characters.map {
                    RemoteKeyEntity(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                remoteKeyDao.insertAll(keys)
                characterDao.insertAll(characters.map { it.toEntity() })
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, CharacterEntity>): RemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { character ->
                remoteKeyDao.remoteKeyId(character.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, CharacterEntity>): RemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { character ->
                remoteKeyDao.remoteKeyId(character.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, CharacterEntity>
    ): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeyDao.remoteKeyId(id)
            }
        }
    }

    private fun getKey(url: String?) = url?.substringAfterLast("=")?.toIntOrNull()
}
