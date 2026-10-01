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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
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
import com.glootie.meeseeks.domain.model.CharacterStatus
import com.glootie.meeseeks.ui.common.UiState
import com.glootie.meeseeks.ui.component.AttributeCard
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
                    message = state.message ?: stringResource(R.string.generic_error),
                    onClickRetry = { viewModel.getCharacterDetails() }
                )
            }

            is UiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            with(sharedTransitionScope) {
                                AsyncImage(
                                    model = state.data.image,
                                    contentDescription = stringResource(R.string.character_thumbnail_content_description),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .width(110.dp)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .sharedElement(
                                            sharedContentState = rememberSharedContentState(
                                                key = IMAGE_SHARED_KEY + characterId
                                            ),
                                            animatedVisibilityScope = animatedVisibilityScope
                                        )
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = state.data.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    SuggestionChip(
                                        onClick = { },
                                        label = { Text(state.data.species) }
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AttributeCard(
                            modifier = Modifier.weight(1f),
                            title = stringResource(R.string.gender_title),
                            body= state.data.gender,
                            icon = Icons.Default.Person,
                            contentDescription = stringResource(R.string.gender_icon_content_description)
                        )
                        AttributeCard(
                            modifier = Modifier.weight(1f),
                            title = stringResource(R.string.status_title),
                            body = state.data.status.name,
                            icon = getIconForStatus(state.data.status),
                            contentDescription = stringResource(R.string.status_icon_content_description)

                        )
                    }

                    Text(
                        text = stringResource(R.string.locations_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    AttributeCard(
                        title = stringResource(R.string.origin_title),
                        body = state.data.origin,
                        icon = Icons.Default.Place,
                        contentDescription = stringResource(R.string.location_icon_content_description)
                    )

                    AttributeCard(
                        title = stringResource(R.string.last_known_location_title),
                        body = state.data.location,
                        icon = Icons.Default.LocationOn,
                        contentDescription = stringResource(R.string.location_icon_content_description)
                    )
                }
            }
        }
    }
}

private fun getIconForStatus(status: CharacterStatus) =
    when (status) {
        CharacterStatus.ALIVE ->  Icons.Default.Favorite
        CharacterStatus.DEAD -> Icons.Default.Close
        CharacterStatus.UNKNOWN -> Icons.Default.Search
    }