package com.glootie.meeseeks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation3.runtime.rememberNavBackStack
import com.glootie.meeseeks.ui.component.MainTopBar
import com.glootie.meeseeks.ui.navigation.NavHost
import com.glootie.meeseeks.ui.navigation.Routes
import com.glootie.meeseeks.ui.theme.MeeSEEKsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        var keepSplashScreen = true
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }
        keepSplashScreen = false // TODO: set when initial load complete

        setContent {
            MeeSEEKsTheme {
                MeeSEEKsApp()
            }
        }
    }
}

@Composable
fun MeeSEEKsApp() {
    val backStack = rememberNavBackStack(Routes.CharacterList)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MainTopBar(title = "MeeSEEKs", isLastScreen = backStack.size == 1, onBack = { backStack.removeLastOrNull() }) }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            backStack = backStack
        )
    }
}