package com.atri.composemusic.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.atri.composemusic.feature.home.HomeBaseRoute
import com.atri.composemusic.feature.home.homeScreen
import com.atri.composemusic.feature.home.navigateToHome
import com.atri.composemusic.feature.musicplay.MiniPlayerOverlay
import com.atri.composemusic.feature.myfavourite.navigation.myFavouriteScreen
import com.atri.composemusic.feature.myfavourite.navigation.navigateToMyFavourite
import com.atri.composemusic.feature.profile.navigation.navigateToProfile
import com.atri.composemusic.feature.profile.navigation.profileScreen
import com.atri.composemusic.navigation.TopLevelDestination
import com.atri.composemusic.ui.CmAppState
import kotlin.reflect.KClass

@Composable
fun MainScreen(
    appState: CmAppState,
    windowAdaptiveInfo: WindowAdaptiveInfo
) {
    val intervalNavController = rememberNavController()
    val navBackStackEntry by intervalNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val layoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(windowAdaptiveInfo)
    val navOption by remember(intervalNavController) {
        derivedStateOf {
            navOptions {
                val startDestinationId = try {
                    intervalNavController.graph.findStartDestination().id
                } catch (e: IllegalStateException) {
                    -1
                }

                if (startDestinationId != -1) {
                    popUpTo(startDestinationId) {
                        saveState = true
                    }
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }
    Box(Modifier.fillMaxSize()) {
        NavigationSuiteScaffold(
            layoutType = layoutType,
            navigationSuiteItems = {
                appState.getTopLevelDestinations(layoutType).forEach { topLevelDestination ->
                    val selected = currentDestination.isRouteInHierarchy(topLevelDestination.route)

                    item(
                        selected = selected,
                        onClick = {
                            when (topLevelDestination) {
                                TopLevelDestination.HOME -> intervalNavController.navigateToHome(navOption)
                                TopLevelDestination.PROFILE -> intervalNavController.navigateToProfile(navOption)
                                TopLevelDestination.MyFavourite -> intervalNavController.navigateToMyFavourite(navOption)
                            }
                        },
                        icon = {
                            if (selected) {
                                Icon(
                                    imageVector = topLevelDestination.selectedIcon,
                                    contentDescription = null
                                )
                            } else {
                                Icon(
                                    imageVector = topLevelDestination.unselectedIcon,
                                    contentDescription = null
                                )
                            }
                        },
                        label = {
                            Text(stringResource(topLevelDestination.iconTextId))
                        }
                    )
                }
            }
        ) {
            Box(Modifier.fillMaxSize()) {
                NavHost(
                    navController = intervalNavController,
                    startDestination = HomeBaseRoute
                ) {
                    homeScreen()
                    profileScreen { }
                    myFavouriteScreen()
                }
            }
        }

        val playerModifier = Modifier.align(Alignment.BottomCenter)
            .then(
                if (layoutType == NavigationSuiteType.NavigationRail) {
                    Modifier.padding(start = 80.dp)
                } else {
                    Modifier
                }
            )
        Box(modifier = playerModifier) {
            MiniPlayerOverlay {
                appState.navigateToMusicPlayer()
            }
        }
    }

}

fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } ?: false
