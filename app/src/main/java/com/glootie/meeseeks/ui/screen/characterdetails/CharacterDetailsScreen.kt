package com.glootie.meeseeks.ui.screen.characterdetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.glootie.meeseeks.R
import com.glootie.meeseeks.core.IMAGE_SHARED_KEY
import com.glootie.meeseeks.core.LOCATION_SHARED_KEY
import com.glootie.meeseeks.domain.model.CharacterDetails
import com.glootie.meeseeks.domain.model.CharacterLastLocation
import com.glootie.meeseeks.domain.model.CharacterOriginLocation
import com.glootie.meeseeks.domain.model.CharacterStatus
import com.glootie.meeseeks.ui.common.UiState
import com.glootie.meeseeks.ui.common.skeleton.AttributeCardSkeleton
import com.glootie.meeseeks.ui.common.skeleton.HeaderCardSkeleton
import com.glootie.meeseeks.ui.component.AttributeCard
import com.glootie.meeseeks.ui.component.shimmerEffect
import com.glootie.meeseeks.ui.theme.MeeSEEKsTheme

@Composable
fun CharacterDetailsScreen(
    characterId: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: CharacterDetailsViewModel = hiltViewModel(
        creationCallback = { factory: CharacterDetailsViewModel.Factory ->
            factory.create(characterId)
        }
    ),
    onLocationClick: (Int) -> Unit = {}
) {
    val characterDetailsUiState by viewModel.characterDetailsUiState.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        when (val state = characterDetailsUiState) {
            is UiState.Loading -> {
                CharacterDetailsSkeletonContent()
            }

            is UiState.Error -> {
                CharacterDetailsErrorContent(
                    retry = viewModel::getCharacterDetails
                )
            }

            is UiState.Success -> {
                CharacterDetailsContent(
                    characterId = characterId,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    characterDetails = state.data,
                    onLocationClick = onLocationClick
                )
            }
        }
    }
}

@Composable
private fun CharacterDetailsContent(
    characterId: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    characterDetails: CharacterDetails,
    onLocationClick: (Int) -> Unit
) {
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
                containerColor = MaterialTheme.colorScheme.surfaceContainer
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
                        model = characterDetails.image,
                        contentDescription = stringResource(R.string.character_thumbnail_content_description),
                        contentScale = ContentScale.Crop,
                        error = painterResource(R.drawable.ic_image_placeholder),
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
                        text = characterDetails.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SuggestionChip(
                            onClick = {},
                            label = { Text(characterDetails.species) }
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
                body = characterDetails.gender,
                icon = Icons.Default.Person,
                contentDescription = stringResource(R.string.gender_icon_content_description)
            )
            AttributeCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.status_title),
                body = characterDetails.status.name,
                icon = getIconForStatus(characterDetails.status),
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

        val originId = characterDetails.originLocation.id
        with(sharedTransitionScope) {
            AttributeCard(
                title = stringResource(R.string.origin_title),
                body = characterDetails.originLocation.name,
                icon = Icons.Default.Place,
                contentDescription = stringResource(R.string.location_icon_content_description),
                onClick = { originId?.let { onLocationClick(it) } },
                modifier = if (originId != null) {
                    Modifier.sharedElement(
                        sharedContentState = rememberSharedContentState(
                            key = LOCATION_SHARED_KEY + originId
                        ),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                } else {
                    Modifier
                }
            )
        }

        val lastLocationId = characterDetails.lastLocation.id
        with(sharedTransitionScope) {
            AttributeCard(
                title = stringResource(R.string.last_known_location_title),
                body = characterDetails.lastLocation.name,
                icon = Icons.Filled.PersonSearch,
                contentDescription = stringResource(R.string.location_icon_content_description),
                onClick = { lastLocationId?.let { onLocationClick(it) } },
                modifier = if (lastLocationId != null) {
                    Modifier.sharedElement(
                        sharedContentState = rememberSharedContentState(
                            key = LOCATION_SHARED_KEY + lastLocationId
                        ),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                } else {
                    Modifier
                }
            )
        }
    }
}

@Composable
private fun CharacterDetailsSkeletonContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HeaderCardSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            hasImage = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AttributeCardSkeleton(
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
            )
            AttributeCardSkeleton(
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color = MaterialTheme.colorScheme.surface)
                .shimmerEffect()
        )
        AttributeCardSkeleton(modifier = Modifier.height(70.dp))
        AttributeCardSkeleton(modifier = Modifier.height(70.dp))
    }
}

@Composable
private fun CharacterDetailsErrorContent(
    retry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.scale(2f),
            imageVector = Icons.Filled.ErrorOutline,
            contentDescription = stringResource(R.string.network_error_content_description),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.generic_network_error_retry),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier.fillMaxWidth(0.6f),
            onClick = retry
        ) {
            Text(
                text = stringResource(R.string.generic_retry),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

private fun getIconForStatus(status: CharacterStatus) =
    when (status) {
        CharacterStatus.ALIVE -> Icons.Default.Favorite
        CharacterStatus.DEAD -> Icons.Default.Close
        CharacterStatus.UNKNOWN -> Icons.Default.QuestionMark
    }

@Preview(showBackground = true)
@Composable
private fun CharacterDetailsContentPreview() {
    SharedTransitionLayout {
        AnimatedVisibility(visible = true) {
            MeeSEEKsTheme {
                CharacterDetailsContent(
                    characterId = 361,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedVisibility,
                    characterDetails = CharacterDetails(
                        name = "Adjudicator Rick",
                        status = CharacterStatus.DEAD,
                        image = null,
                        species = "Human",
                        gender = "Male",
                        originLocation = CharacterOriginLocation(1, "unknown"),
                        lastLocation = CharacterLastLocation(2, "Citadel of Ricks"),
                    ),
                    onLocationClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CharacterDetailsErrorContentPreview() {
    MeeSEEKsTheme {
        Box(Modifier.fillMaxSize()) {
            CharacterDetailsErrorContent(
                retry = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CharacterDetailsSkeletonContentPreview() {
    MeeSEEKsTheme {
        CharacterDetailsSkeletonContent()
    }
}