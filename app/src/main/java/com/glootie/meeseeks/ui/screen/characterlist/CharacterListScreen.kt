package com.glootie.meeseeks.ui.screen.characterlist

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.glootie.meeseeks.R
import com.glootie.meeseeks.core.CHARACTER_CARD_CONTENT_TYPE
import com.glootie.meeseeks.core.ERROR_CARD_CONTENT_TYPE
import com.glootie.meeseeks.core.LOADING_INDICATOR_CONTENT_TYPE
import com.glootie.meeseeks.domain.model.CharacterSummary
import com.glootie.meeseeks.ui.common.UiState
import com.glootie.meeseeks.ui.component.CharacterCard
import com.glootie.meeseeks.ui.component.ListErrorCard

@Composable
fun CharacterListScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: CharacterListViewModel = hiltViewModel(),
    searchQuery: String = "",
    onItemClick: (Int) -> Unit = {}
) {
    val gridState = rememberLazyGridState()

    LaunchedEffect(searchQuery) {
        viewModel.updateSearchQuery(searchQuery)
        gridState.scrollToItem(0)
    }

    val characterList = viewModel.characterList.collectAsLazyPagingItems()

    val uiState by remember {
        derivedStateOf {
            when (val refreshState = characterList.loadState.refresh) {
                is LoadState.Loading -> UiState.Loading
                is LoadState.Error -> UiState.Error(
                    message = refreshState.error.localizedMessage,
                    throwable = refreshState.error
                )

                else -> UiState.Success(Unit)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is UiState.Error -> {
                ListErrorCard(
                    modifier = Modifier.align(Alignment.Center),
                    message = (uiState as UiState.Error).message
                        ?: stringResource(R.string.generic_error),
                    onClickRetry = { characterList.refresh() }
                )
            }

            is UiState.Success -> {
                CharacterSummaryList(
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    modifier = Modifier.fillMaxSize(),
                    characterList = characterList,
                    gridState = gridState,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

@Composable
fun CharacterSummaryList(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    characterList: LazyPagingItems<CharacterSummary>,
    gridState: LazyGridState = rememberLazyGridState(),
    onItemClick: (Int) -> Unit = {}
) {
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Adaptive(minSize = 340.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            count = characterList.itemCount,
            key = characterList.itemKey { it.id },
            contentType = { CHARACTER_CARD_CONTENT_TYPE }
        ) { index ->
            characterList[index]?.let { character ->
                CharacterCard(
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    characterSummary = character,
                    index = index + 1,
                    onClick = { onItemClick(character.id) }
                )
            }
        }

        when (val appendState = characterList.loadState.append) {
            is LoadState.Loading -> {
                item(
                    span = { GridItemSpan(maxLineSpan) },
                    contentType = { LOADING_INDICATOR_CONTENT_TYPE }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }

            is LoadState.Error -> {
                item(
                    span = { GridItemSpan(maxLineSpan) },
                    contentType = { ERROR_CARD_CONTENT_TYPE }
                ) {
                    ListErrorCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        message = appendState.error.localizedMessage.ifBlank { stringResource(R.string.generic_error) }
                            ?: stringResource(R.string.generic_error),
                        onClickRetry = { characterList.retry() }
                    )
                }
            }

            else -> {}
        }
    }
}
