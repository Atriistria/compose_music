package com.atri.composemusic.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.atri.composemusic.feature.main.navigation.MainRoute
import com.atri.composemusic.feature.main.navigation.mainScreen
import com.atri.composemusic.feature.musicplay.MiniPlayerOverlay
import com.atri.composemusic.feature.musicplay.MusicPlayerScreen
import com.atri.composemusic.feature.myfavourite.navigation.myFavouriteScreen
import com.atri.composemusic.feature.settings.navigation.settingsScreen
import com.atri.composemusic.ui.CmAppState

@Composable
fun CmNavHost(
    appState: CmAppState,
    windowAdaptiveInfo: WindowAdaptiveInfo,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController

    var isPlayerExpanded by rememberSaveable { mutableStateOf(false) }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    val layoutType = with(windowAdaptiveInfo) {
        if (windowSizeClass.isWidthAtLeastBreakpoint(840)) {
            NavigationSuiteType.NavigationDrawer
        } else {
            NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(windowAdaptiveInfo)
        }
    }

    Box(Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = MainRoute,
            modifier = modifier
        ) {
            mainScreen(appState, windowAdaptiveInfo)
            myFavouriteScreen()
            settingsScreen()
        }

        val playerModifier = Modifier
            .align(Alignment.BottomCenter)
            .then(
                when (layoutType) {
                    NavigationSuiteType.NavigationRail -> Modifier.padding(start = 80.dp)

                    NavigationSuiteType.NavigationDrawer -> Modifier.padding(start = 360.dp)

                    else -> Modifier
                }
            )
            .fillMaxWidth()

        if (currentDestination?.hasRoute(MainRoute::class) == true) {
            Box(modifier = playerModifier) {
                MiniPlayerOverlay {
                    isPlayerExpanded = true

                }
            }
        }

        AnimatedVisibility(
            visible = isPlayerExpanded,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.fillMaxSize() // 盖住全屏
        ) {
            BackHandler(enabled = isPlayerExpanded) {
                isPlayerExpanded = false
            }
            MusicPlayerScreen(
                onBack = {
                    isPlayerExpanded = false
                }
            )
        }

    }

}