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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.glootie.meeseeks.ui.component.MainTopBar
import com.glootie.meeseeks.ui.navigation.NavHost
import com.glootie.meeseeks.ui.navigation.Routes
import com.glootie.meeseeks.ui.theme.MeeSEEKsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        var keepSplashScreen = true
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFirstPageLoaded.collect {
                    keepSplashScreen = false
                }
            }
        }

        splashScreen.setKeepOnScreenCondition { keepSplashScreen }
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MainTopBar(
                title = stringResource(R.string.app_name),
                isLastScreen = getIsLastScreen(backStack),
                onBack = { backStack.removeLastOrNull() })
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            backStack = backStack
        )
    }
}

private fun getIsLastScreen(backStack: NavBackStack<NavKey>): Boolean = backStack.size == 1
