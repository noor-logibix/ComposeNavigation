package com.example.composenavigation

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.composenavigation.ui.screens.GameScreen
import com.example.composenavigation.ui.screens.HomeScreen
import com.example.composenavigation.ui.screens.ScoreScreen
import com.example.composenavigation.ui.theme.ComposeNavigationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeNavigationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = Route.HOME.name) {
        composable(Route.HOME.name) {
            val activity = LocalContext.current as Activity
            HomeScreen(
                onNext = {
                    navHostController.navigate(Route.GAME.name)
                },
                onBack = {
                    activity.finish()
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        composable(Route.GAME.name) {
            val score = 99
            GameScreen(
                onNext = {
                    navHostController.navigate("${Route.SCORE.name}/$score")
                },
                onBack = {
                    navHostController.popBackStack()
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        composable("${Route.SCORE.name}/{score}", arguments = listOf(
            navArgument("score") {
                type = NavType.IntType
            }
        )) {
            val activity = LocalContext.current as Activity
            val score = it.arguments?.getInt("score")
            ScoreScreen(
                score = score ?: 0,
                onExit = {
                    activity.finish()
                },
                onReturnToHome = {
                    navHostController.popBackStack(Route.HOME.name, false)
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

enum class Route {
    HOME,
    GAME,
    SCORE
}

