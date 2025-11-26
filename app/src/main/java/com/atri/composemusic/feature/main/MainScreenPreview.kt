package com.atri.composemusic.feature.main

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.atri.composemusic.ui.rememberCmAppState


@Preview(device = "spec:width=1920dp,height=1080dp,dpi=160")
@Composable
fun MainScreenPreview(
) {
    val appState = rememberCmAppState(networkMonitor = { kotlinx.coroutines.flow.flowOf(true) })
    MainScreen(
        appState = appState,
        windowAdaptiveInfo = currentWindowAdaptiveInfo()
    )
}
