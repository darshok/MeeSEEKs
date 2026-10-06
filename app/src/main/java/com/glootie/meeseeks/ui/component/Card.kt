package com.glootie.meeseeks.ui.component


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.glootie.meeseeks.R
import com.glootie.meeseeks.core.IMAGE_SHARED_KEY
import com.glootie.meeseeks.domain.model.CharacterStatus
import com.glootie.meeseeks.domain.model.CharacterSummary

@Composable
fun CharacterCard(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    characterSummary: CharacterSummary,
    index: Int,
    onClick: () -> Unit = {}
) {
    val hasImage = !characterSummary.image.isNullOrBlank()
    var isLoaded by remember { mutableStateOf(!hasImage) }
    val alpha by animateFloatAsState(
        targetValue = if (isLoaded) 1f else 0f,
        animationSpec = tween(durationMillis = 300)
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Box(modifier = Modifier.graphicsLayer { this.alpha = alpha }) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, topEnd = 8.dp))
                    .background(MaterialTheme.colorScheme.onPrimaryContainer)
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = index.toString(),
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                with(sharedTransitionScope) {
                    AsyncImage(
                        modifier = Modifier
                            .padding(8.dp)
                            .width(110.dp)
                            .aspectRatio(3f / 4f)
                            .clip(RoundedCornerShape(8.dp))
                            .sharedElement(
                                sharedContentState = rememberSharedContentState(key = IMAGE_SHARED_KEY + characterSummary.id),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
                        contentScale = ContentScale.Crop,
                        model = characterSummary.image,
                        error = painterResource(R.drawable.ic_image_placeholder),
                        contentDescription = stringResource(R.string.character_thumbnail_content_description),
                        onSuccess = { isLoaded = true },
                        onError = { isLoaded = true }
                    )
                }
                Column(modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()) {
                    Text(
                        text = characterSummary.name,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = characterSummary.status.name,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun ListErrorCard(
    modifier: Modifier = Modifier,
    message: String,
    onClickRetry: () -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primaryContainer
        )
        Button(onClick = onClickRetry) {
            Text(text = stringResource(R.string.generic_retry))
        }
    }
}

@Composable
fun AttributeCard(
    modifier: Modifier = Modifier,
    title: String,
    body: String,
    icon: ImageVector,
    contentDescription: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview
@Composable
private fun AttributeCardPreview() {
    AttributeCard(
        title = "Gender",
        body = "Male",
        icon = Icons.Default.Person,
        contentDescription = "content"
    )
}

@Preview
@Composable
private fun CharacterCardPreview() {
    SharedTransitionLayout {
        AnimatedVisibility(visible = true) {
            CharacterCard(
                sharedTransitionScope = this@SharedTransitionLayout,
                animatedVisibilityScope = this@AnimatedVisibility,
                characterSummary = CharacterSummary(
                    id = 361,
                    name = "Toxic Rick",
                    status = CharacterStatus.DEAD,
                    image = null,
                ),
                index = 1
            )
        }
    }
}
