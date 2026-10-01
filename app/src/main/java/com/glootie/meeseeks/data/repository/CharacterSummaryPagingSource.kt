package com.glootie.meeseeks.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.glootie.meeseeks.core.DataResponse
import com.glootie.meeseeks.data.entity.response.mapToDomain
import com.glootie.meeseeks.domain.model.CharacterSummary
import java.io.IOException
import javax.inject.Inject

class CharacterSummaryPagingSource @Inject constructor(
    private val repository: CharacterRepository,
) :
    PagingSource<Int, CharacterSummary>() {
    override fun getRefreshKey(state: PagingState<Int, CharacterSummary>) =
        state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterSummary> {
        return try {
            val page = params.key ?: 1
            when (val dataResponse = repository.getCharacters(page)) {
                    is DataResponse.Error -> LoadResult.Error(Exception(dataResponse.error))
                    is DataResponse.Success ->{
                        val characterList = dataResponse.data.mapToDomain()
                        LoadResult.Page(
                            data = characterList.results,
                            prevKey = characterList.info.prev?.let { getKey(it) },
                            nextKey = getKey(characterList.info.next)
                        )
                    }
                }
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        }
    }

    private fun getKey(url: String) = url.substringAfterLast("=").toIntOrNull()
}