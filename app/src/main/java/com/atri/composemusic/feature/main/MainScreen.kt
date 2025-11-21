package com.atri.composemusic.feature.main

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.atri.composemusic.feature.home.HomeScreen
import com.atri.composemusic.feature.musicplay.MiniPlayerOverlay
import com.atri.composemusic.feature.profile.ProfileScreen
import com.atri.composemusic.feature.settings.SettingScreen
import com.atri.composemusic.navigation.NavigationViewModel
import com.atri.composemusic.navigation.Screen

sealed class BottomNavScreen(val route: String, val label: String, val icon: ImageVector) {
    data object Home : BottomNavScreen("home", "首页", Icons.Default.Home)
    data object Profile : BottomNavScreen("profile", "我的", Icons.Default.Person)
    data object Settings : BottomNavScreen("settings", "设置", Icons.Default.Settings)
}

@Composable
fun MainScreen(
    navigationViewModel: NavigationViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val listState = rememberLazyListState()
    val headShow = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    val bottomNavItems = remember {
        listOf(
            BottomNavScreen.Home,
            BottomNavScreen.Profile,
            BottomNavScreen.Settings,
        )
    }

    Box(Modifier.fillMaxSize()) {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                bottomNavItems.forEach { screen ->
                    item(
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) }
                    )
                }
            },
            // navigationSuiteColors = NavigationSuiteDefaults.colors(...)
        ) {
            MainContent(navController, navigationViewModel)
        }

        AnimatedVisibility(
            visible = headShow.value,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            ) { it / 2 } + fadeIn(
                animationSpec = tween(durationMillis = 300)
            ),
            exit = slideOutVertically(
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            ) { it / 2 } + fadeOut(
                animationSpec = tween(durationMillis = 300)
            )
        ) {
            MiniPlayerOverlay { navigationViewModel.navigateTo(Screen.MusicPlayer.route) }
        }
    }
}

@Composable
fun MainContent(
    navController: NavHostController,
    navigationViewModel: NavigationViewModel
) {
    NavHost(
        navController = navController,
        graph = remember(navController) {
            navController.createGraph(
                startDestination = BottomNavScreen.Home.route,
                route = "bottom_root"
            ) {
                composable(BottomNavScreen.Home.route) {
                    HomeScreen()
                }

                composable(BottomNavScreen.Profile.route) {
                    ProfileScreen(navigationViewModel)
                }

                composable(BottomNavScreen.Settings.route) {
                    SettingScreen()
                }
            }
        },
        modifier = Modifier.padding()
    )
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(apiLevel = 34, device = "spec:width=673dp,height=841dp")
@Composable
fun MainScreenPreview() {
    val textFieldState = rememberTextFieldState()
    val searchResults = remember { listOf("示例结果1", "示例结果2") }
    val viewModel = NavigationViewModel()
    MainScreen(viewModel)
}
