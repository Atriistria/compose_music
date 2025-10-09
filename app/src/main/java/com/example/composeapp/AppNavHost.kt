package com.example.composeapp

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeapp.ui.screen.LoginScreen
import com.example.composeapp.ui.screen.MainScreen
import com.example.composeapp.ui.screen.MusicPlayerScreen
import com.example.composeapp.ui.screen.MyFavor
import com.example.composeapp.ui.viewmodel.NavigationViewModel

@Composable
fun AppNavHost(
    navigationViewModel: NavigationViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        navigationViewModel.navigationEvent.collect { event ->
            when(event) {
                is NavigationEvent.Navigate -> navController.navigate(event.route)
                is NavigationEvent.NavigateUp -> navController.navigateUp()
                is NavigationEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }

    NavHost(navController, Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen {
                navigationViewModel.navigateTo(Screen.Main.route)
            }
        }

        composable(
            Screen.Main.route,
        ) { MainScreen(navigationViewModel) }

        composable(
            Screen.MyFavor.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }, // 比如离开时淡出
            popEnterTransition = { fadeIn(animationSpec = tween(300)) }, // 返回时淡入
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
        ) { MyFavor(navigationViewModel) }

        composable(
            Screen.MusicPlayer.route,
            enterTransition = { slideInVertically(initialOffsetY = { it }) },
            exitTransition = { slideOutVertically(targetOffsetY = { it }) },
            popEnterTransition = { slideInVertically(initialOffsetY = { it }) },
            popExitTransition = { slideOutVertically(targetOffsetY = { it }) }
        ) { MusicPlayerScreen(navigationViewModel) }

    }
}