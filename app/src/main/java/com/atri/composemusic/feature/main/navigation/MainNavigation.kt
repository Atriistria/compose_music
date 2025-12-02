package com.atri.composemusic.feature.main.navigation

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.atri.composemusic.feature.main.MainScreen
import com.atri.composemusic.ui.CmAppState
import kotlinx.serialization.Serializable

@Serializable data object MainRoute

@Serializable data object MainBaseRoute

fun NavController.navigateToMainScreen(navOptions: NavOptions? = null) = navigate(MainRoute, navOptions)

fun NavGraphBuilder.mainScreen(
    appState: CmAppState,
    windowAdaptiveInfo: WindowAdaptiveInfo
) {
    composable<MainRoute> {
        MainScreen(appState, windowAdaptiveInfo)
    }
}