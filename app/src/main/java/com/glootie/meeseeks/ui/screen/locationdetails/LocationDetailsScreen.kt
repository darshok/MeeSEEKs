package com.glootie.meeseeks.ui.screen.locationdetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.glootie.meeseeks.R
import com.glootie.meeseeks.core.LOCATION_SHARED_KEY
import com.glootie.meeseeks.domain.model.LocationDetails
import com.glootie.meeseeks.ui.common.UiState
import com.glootie.meeseeks.ui.common.skeleton.AttributeCardSkeleton
import com.glootie.meeseeks.ui.common.skeleton.HeaderCardSkeleton
import com.glootie.meeseeks.ui.component.AttributeCard
import com.glootie.meeseeks.ui.component.HeaderCard
import com.glootie.meeseeks.ui.theme.MeeSEEKsTheme

@Composable
fun LocationDetailsScreen(
    locationId: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: LocationDetailsViewModel = hiltViewModel(
        creationCallback = { factory: LocationDetailsViewModel.Factory ->
            factory.create(locationId)
        }
    )
) {
    val locationDetailsUiState by viewModel.locationDetailsUiState.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        when (val state = locationDetailsUiState) {
            is UiState.Loading -> {
                LocationDetailsSkeletonContent()
            }

            is UiState.Error -> {
                LocationDetailsErrorContent(
                    retry = viewModel::getLocationDetails
                )
            }

            is UiState.Success -> {
                LocationDetailsContent(
                    locationId = locationId,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    locationDetails = state.data
                )
            }
        }
    }
}

@Composable
private fun LocationDetailsContent(
    locationId: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    locationDetails: LocationDetails
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HeaderCard {
            with(sharedTransitionScope) {
                Text(
                    text = locationDetails.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.sharedElement(
                        sharedContentState = rememberSharedContentState(
                            key = LOCATION_SHARED_KEY + locationId
                        ),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                )
            }
            SuggestionChip(
                onClick = {},
                label = { Text(locationDetails.type) }
            )
        }

        AttributeCard(
            title = stringResource(R.string.dimension_title),
            body = locationDetails.dimension,
            icon = Icons.Default.Public,
            contentDescription = stringResource(R.string.dimension_icon_content_description)
        )
    }
}

@Composable
private fun LocationDetailsSkeletonContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HeaderCardSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp), hasImage = true
        )
        AttributeCardSkeleton(modifier = Modifier.height(70.dp))
    }
}

@Composable
private fun LocationDetailsErrorContent(
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

@Preview(showBackground = true)
@Composable
private fun LocationDetailsContentPreview() {
    SharedTransitionLayout {
        AnimatedVisibility(visible = true) {
            MeeSEEKsTheme {
                LocationDetailsContent(
                    locationId = 1,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedVisibility,
                    locationDetails = LocationDetails(
                        name = "Earth (C-137)",
                        type = "Planet",
                        dimension = "Dimension C-137"
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationDetailsErrorContentPreview() {
    MeeSEEKsTheme {
        Box(Modifier.fillMaxSize()) {
            LocationDetailsErrorContent(
                retry = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationDetailsSkeletonContentPreview() {
    MeeSEEKsTheme {
        LocationDetailsSkeletonContent()
    }
}
