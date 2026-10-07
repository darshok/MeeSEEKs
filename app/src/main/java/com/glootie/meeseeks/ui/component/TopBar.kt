package com.glootie.meeseeks.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.glootie.meeseeks.R
import com.glootie.meeseeks.core.SEARCH_BAR_KEY

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    title: String,
    isLastScreen: Boolean,
    onBack: () -> Unit,
    showSearchAction: Boolean = false,
    isSearchActive: Boolean = false,
    onSearchActiveChange: (Boolean) -> Unit = {},
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {}
) {
    AnimatedContent(
        targetState = isSearchActive,
        transitionSpec = {
            if (targetState) {
                fadeIn() togetherWith (shrinkOut(shrinkTowards = Alignment.TopEnd) + fadeOut())
            } else {
                (expandIn(expandFrom = Alignment.TopEnd) + fadeIn()) togetherWith fadeOut()
            }
        },
        label = SEARCH_BAR_KEY
    ) { active ->
        if (active) {
            val searchBarState = rememberSearchBarState()
            val focusRequester = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current
            val textFieldState = remember { TextFieldState() }

            LaunchedEffect(Unit) {
                textFieldState.setTextAndPlaceCursorAtEnd(searchQuery)
                focusRequester.requestFocus()
            }

            LaunchedEffect(textFieldState.text) {
                val newQuery = textFieldState.text.toString()
                if (searchQuery != newQuery) {
                    onSearchQueryChange(newQuery)
                }
            }

            TopSearchBar(
                state = searchBarState,
                modifier = Modifier.fillMaxWidth(),
                inputField = {
                    SearchBarDefaults.InputField(
                        state = textFieldState,
                        onSearch = { focusManager.clearFocus(); onSearchActiveChange(false) },
                        expanded = false,
                        onExpandedChange = { },
                        modifier = Modifier.focusRequester(focusRequester),
                        placeholder = { Text(stringResource(R.string.search_placeholder)) },
                        leadingIcon = {
                            IconButton(onClick = {
                                onSearchActiveChange(false)
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.back_navigation)
                                )
                            }
                        },
                        trailingIcon = {
                            if (textFieldState.text.isNotEmpty()) {
                                IconButton(onClick = {
                                    textFieldState.setTextAndPlaceCursorAtEnd("")
                                    onSearchQueryChange("")
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = stringResource(R.string.clear_search)
                                    )
                                }
                            }
                        }
                    )
                }
            )
        } else {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        modifier = Modifier.height(TopAppBarDefaults.MediumAppBarCollapsedHeight),
                        painter = painterResource(id = R.drawable.ic_rick_and_morty_logo),
                        contentDescription = title,
                        contentScale = ContentScale.Crop
                    )
                },
                navigationIcon = {
                    AnimatedVisibility(
                        visible = !isLastScreen,
                        enter = fadeIn() + expandHorizontally(),
                        exit = fadeOut() + shrinkHorizontally()
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back_navigation),
                            )
                        }
                    }
                },
                actions = {
                    if (showSearchAction) {
                        IconButton(onClick = { onSearchActiveChange(true) }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(R.string.search_action)
                            )
                        }
                    }
                }
            )
        }
    }
}
