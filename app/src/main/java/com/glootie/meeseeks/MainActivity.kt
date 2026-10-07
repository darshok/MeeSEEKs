package com.glootie.meeseeks

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation3.runtime.rememberNavBackStack
import com.glootie.meeseeks.ui.component.MainTopBar
import com.glootie.meeseeks.ui.navigation.NavHost
import com.glootie.meeseeks.ui.navigation.Routes
import com.glootie.meeseeks.ui.theme.MeeSEEKsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { !viewModel.isFirstPageLoaded.value }
        splashScreen.setOnExitAnimationListener { listener ->
            val zoomX = ObjectAnimator.ofFloat(
                listener.iconView,
                View.SCALE_X,
                0.6f,
                0.0f
            ).apply {
                interpolator = OvershootInterpolator()
                duration = 500L
                doOnEnd { listener.remove() }
            }
            val zoomY = ObjectAnimator.ofFloat(
                listener.iconView,
                View.SCALE_Y,
                0.6f,
                0.0f
            ).apply {
                interpolator = OvershootInterpolator()
                duration = 500L
                doOnEnd { listener.remove() }
            }
            zoomX.start()
            zoomY.start()
        }

        setContent {
            MeeSEEKsTheme {
                MeeSEEKsApp()
            }
        }
    }
}

@Composable
private fun MeeSEEKsApp() {
    val backStack = rememberNavBackStack(Routes.CharacterList)
    val isLastScreen = backStack.size == 1

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isSearchActive by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MainTopBar(
                title = stringResource(R.string.app_name),
                isLastScreen = isLastScreen,
                onBack = { backStack.removeLastOrNull() },
                showSearchAction = isLastScreen,
                isSearchActive = isSearchActive,
                onSearchActiveChange = { isSearchActive = it },
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            backStack = backStack,
            searchQuery = searchQuery,
            isSearchActive = { isSearchActive = it }
        )
    }
}
