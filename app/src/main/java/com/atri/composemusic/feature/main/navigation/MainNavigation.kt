package com.atri.composemusic.feature.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.atri.composemusic.feature.main.MainScreen
import kotlinx.serialization.Serializable

@Serializable data object MainScreenRoute

@Serializable data object MainScreenBaseRoute

fun NavController.navigateToMainScreen(navOptions: NavOptions? = null) = navigate(MainScreenRoute, navOptions)

fun NavGraphBuilder.mainScreen() {
    composable<MainScreenRoute> {
        MainScreen()
    }
}