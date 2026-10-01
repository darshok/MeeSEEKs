package com.glootie.meeseeks.ui.screen.characterdetails

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.glootie.meeseeks.R
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
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.fillMaxWidth()) {
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
                                    contentDescription = stringResource(R.string.character_thumbnail),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .width(110.dp)
                                        .aspectRatio(3f / 4f)
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
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
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
                                    state.data.gender
                                )
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
