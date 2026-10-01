package com.glootie.meeseeks.ui.component


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.glootie.meeseeks.R
import com.glootie.meeseeks.core.IMAGE_SHARED_KEY
import com.glootie.meeseeks.domain.model.CharacterSummary

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CharacterCard(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    characterSummary: CharacterSummary,
    index: Int,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        CardContent(
            modifier = Modifier.padding(8.dp),
            id = characterSummary.id,
            image = characterSummary.image,
            title = characterSummary.name,
            body = characterSummary.status,
            index = index,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun CardContent(
    modifier: Modifier,
    id: Int,
    title: String,
    image: String?,
    body: String,
    index: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val hasImage = !image.isNullOrBlank()
    var isLoaded by remember { mutableStateOf(!hasImage) }
    val alpha by animateFloatAsState(
        targetValue = if (isLoaded) 1f else 0f,
        label = "loadingTransition",
        animationSpec = tween(durationMillis = 300)
    )
    Box(modifier = Modifier.graphicsLayer { this.alpha = alpha }) {
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clip(RoundedCornerShape(bottomStart = 16.dp, topEnd = 8.dp))
                .background(MaterialTheme.colorScheme.onPrimaryContainer)
        ) {
            Text(
                modifier = modifier,
                text = index.toString(),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
        Row(
            modifier = modifier.padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            with(sharedTransitionScope) {
                AsyncImage(
                    modifier = modifier
                        .width(110.dp)
                        .aspectRatio(3f / 4f)
                        .clip(RoundedCornerShape(8.dp))
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(key = IMAGE_SHARED_KEY + id),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                    contentScale = ContentScale.Crop,
                    model = image,
                    contentDescription = stringResource(R.string.character_thumbnail),
                    onSuccess = { isLoaded = true },
                )
            }
            Column(modifier = modifier.fillMaxWidth()) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun CharacterCardPreview() {
    SharedTransitionLayout {
        AnimatedVisibility(visible = true) {
            CharacterCard(
                sharedTransitionScope = this@SharedTransitionLayout,
                animatedVisibilityScope = this,
                characterSummary = CharacterSummary(
                    id = 361,
                    name = "Toxic Rick",
                    status = "Dead",
                    image = null,
                ),
                index = 1
            )
        }
    }
}
