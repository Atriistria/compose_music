package com.atri.composemusic.ui

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.atri.composemusic.feature.main.navigation.MainRoute
import com.atri.composemusic.feature.main.navigation.mainScreen
import com.atri.composemusic.feature.musicplay.navigation.musicPlayScreen

@Composable
fun CmApp(
    appState: CmAppState,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    NavHost(
        appState.navController,
        startDestination = MainRoute,
    ) {
        mainScreen(appState, windowAdaptiveInfo)
        musicPlayScreen(onBack = { appState.navController.popBackStack() })
    }
}
