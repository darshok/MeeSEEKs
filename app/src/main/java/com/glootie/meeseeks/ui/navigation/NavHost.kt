package com.glootie.meeseeks.ui.navigation

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import com.glootie.meeseeks.ui.screen.characterdetails.CharacterDetailsScreen
import com.glootie.meeseeks.ui.screen.characterlist.CharacterListScreen
import com.glootie.meeseeks.ui.screen.locationdetails.LocationDetailsScreen

@Composable
fun NavHost(
    modifier: Modifier = Modifier,
    backStack: NavBackStack<NavKey>,
) {
    SharedTransitionLayout {
        val entryProvider = entryProvider<NavKey> {
            entry<Routes.CharacterList> {
                CharacterListScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                    onItemClick = {
                        backStack.add(Routes.CharacterDetails(it))
                    }
                )
            }
            entry<Routes.CharacterDetails> { key ->
                CharacterDetailsScreen(
                    characterId = key.id,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                    onLocationClick = {
                        backStack.add(Routes.LocationDetails(it))
                    }
                )
            }
            entry<Routes.LocationDetails> { key ->
                LocationDetailsScreen(
                    locationId = key.id,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = LocalNavAnimatedContentScope.current
                )
            }
        }

        NavDisplay(
            modifier = modifier,
            backStack = backStack,
            entryProvider = entryProvider,
            onBack = { backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberViewModelStoreNavEntryDecorator(),
                rememberSaveableStateHolderNavEntryDecorator(),
            ),
            sharedTransitionScope = this@SharedTransitionLayout,
            popTransitionSpec = {
                fadeIn(animationSpec = tween(500)) togetherWith
                        fadeOut(animationSpec = tween(500))
            },
            predictivePopTransitionSpec = {
                fadeIn(animationSpec = tween(500)) togetherWith
                        fadeOut(animationSpec = tween(500))
            }
        )
    }
}