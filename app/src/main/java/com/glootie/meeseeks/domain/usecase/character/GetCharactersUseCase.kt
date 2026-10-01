package com.glootie.meeseeks.domain.usecase.character

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.glootie.meeseeks.data.repository.CharacterRepository
import com.glootie.meeseeks.data.repository.CharacterSummaryPagingSource
import com.glootie.meeseeks.domain.model.CharacterSummary
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

const val CHARACTER_PREFETCH = 5
const val CHARACTER_PAGE_SIZE = 20

class GetCharactersUseCase @Inject constructor(val repository: CharacterRepository) {
    operator fun invoke(): Flow<PagingData<CharacterSummary>> {
        return Pager(
            config = PagingConfig(
                prefetchDistance = CHARACTER_PREFETCH,
                pageSize = CHARACTER_PAGE_SIZE
            ),
            pagingSourceFactory = {
                CharacterSummaryPagingSource(repository)
            }).flow
    }
}