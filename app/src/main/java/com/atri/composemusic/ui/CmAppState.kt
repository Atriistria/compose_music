package com.atri.composemusic.ui

import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.atri.composemusic.core.data.util.NetworkMonitor
import com.atri.composemusic.feature.musicplay.navigation.navigateToMusicPlay
import com.atri.composemusic.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberCmAppState(
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): CmAppState {
    return remember(
        navController,
        coroutineScope,
        networkMonitor
    ) {
        CmAppState(
            navController = navController,
            coroutineScope = coroutineScope,
            networkMonitor = networkMonitor
        )
    }
}

@Stable
class CmAppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor
) {
    val isOffline = networkMonitor.isOnline()
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    private val compactDestinations = listOf(
        TopLevelDestination.HOME,
        TopLevelDestination.PROFILE
    )

    private val expandedDestinations = listOf(
        TopLevelDestination.HOME,
        TopLevelDestination.MyFavourite,
        TopLevelDestination.PROFILE
    )

    fun getTopLevelDestinations(layoutType: NavigationSuiteType): List<TopLevelDestination> {
        return if (layoutType == NavigationSuiteType.NavigationBar) {
            compactDestinations
        } else {
            expandedDestinations
        }
    }

    fun navigateToMusicPlayer() {
        navController.navigateToMusicPlay(
            navOptions { launchSingleTop = true }
        )
    }

}

