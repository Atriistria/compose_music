package com.atri.composemusic.feature.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.atri.composemusic.feature.home.HomeScreen
import com.atri.composemusic.feature.player.MiniPlayerOverlay
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
        Scaffold(
            topBar = {
            },
            bottomBar = {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
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
                }
            }
        ) { paddingValues ->
            MainContent(navController,paddingValues,listState,navigationViewModel)
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
    paddingValues: PaddingValues,
    listState: LazyListState,
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
                    HomeScreen(listState)
                }

                composable(BottomNavScreen.Profile.route) {
                    ProfileScreen(navigationViewModel)
                }

                composable(BottomNavScreen.Settings.route) {
                    SettingScreen()
                }
            }
        },
        modifier = Modifier.padding(paddingValues)
    )
}

@Preview(apiLevel = 34)
@Composable
fun MainScreenPreview() {
    val textFieldState = rememberTextFieldState()
    val searchResults = remember { listOf("示例结果1", "示例结果2") }
    //MainScreen()
}
