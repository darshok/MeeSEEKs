package com.glootie.meeseeks.ui.screen.characterdetails

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.glootie.meeseeks.core.IMAGE_SHARED_KEY
import com.glootie.meeseeks.ui.common.UiState
import com.glootie.meeseeks.ui.screen.characterlist.ErrorCard

@Composable
fun CharacterDetailsScreen(
    characterId: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: CharacterDetailsViewModel = hiltViewModel(
        creationCallback = { factory: CharacterDetailsViewModel.Factory ->
            factory.create(characterId)
        }
    )
) {
    val characterDetailsUiState by viewModel.characterDetailsUiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = characterDetailsUiState) {
            is UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is UiState.Error -> {
                ErrorCard(
                    modifier = Modifier.align(Alignment.Center),
                    message = state.message ?: "An error occurred",
                    onClickRetry = { /* TODO:Handle retry logic if implemented */ }
                )
            }

            is UiState.Success -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            AsyncImage(
                                model = state.data.image,
                                contentDescription = "Banner Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                            )
                            // Dark overlay to make text more legible
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .background(Color.Black.copy(alpha = 0.5f))
                            )

                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                with(sharedTransitionScope) {
                                    AsyncImage(
                                        model = state.data.image,
                                        contentDescription = "Cover Image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .width(100.dp)
                                            .height(150.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .sharedElement(
                                                sharedContentState = rememberSharedContentState(
                                                    key = IMAGE_SHARED_KEY + characterId
                                                ),
                                                animatedVisibilityScope = animatedVisibilityScope
                                            )
                                    )
                                }

                                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                                    Text(
                                        text = state.data.name,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Column(modifier = Modifier.padding(16.dp)) {
                            val genres = listOf(state.data.species)
                            if (!genres.isNullOrEmpty()) {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(bottom = 16.dp)
                                ) {
                                    items(genres.filterNotNull()) { genre ->
                                        SuggestionChip(
                                            onClick = { },
                                            label = { Text(genre) }
                                        )
                                    }
                                }
                            }
                            Row {
                                SuggestionChip(
                                    onClick = { },
                                    label = { Text(state.data.species) }
                                )
                                SuggestionChip(
                                    onClick = { },
                                    label = { Text(state.data.status.name) }
                                )
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    InfoItem(
                                        "Score",
                                        state.data.gender)
                                    InfoItem("Format", state.data.status.name)
                                    InfoItem("Episodes", state.data.status.name)
                                    InfoItem("Status", state.data.status.name)
                                }
                            }

                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Text(
                                text = state.data.gender,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
