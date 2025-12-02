package com.atri.composemusic.ui

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import com.atri.composemusic.navigation.CmNavHost

@Composable
fun CmApp(
    appState: CmAppState,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    CmNavHost(appState, windowAdaptiveInfo)
}
