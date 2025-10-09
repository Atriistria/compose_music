package com.example.composeapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.example.composeapp.ui.viewmodel.NavigationViewModel


sealed class BottomNavScreen(val route: String, val label: String, val icon: ImageVector) {
    data object Home : BottomNavScreen("home", "首页", Icons.Default.Home)
    data object Profile : BottomNavScreen("profile", "我的", Icons.Default.Person)
    data object Settings : BottomNavScreen("settings", "设置", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navigationViewModel: NavigationViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 添加搜索相关的状态
    val textFieldState = rememberTextFieldState()
    val searchResults = remember { mutableStateOf<List<String>>(emptyList()) }

    val bottomNavItems = listOf(
        BottomNavScreen.Home,
        BottomNavScreen.Profile,
        BottomNavScreen.Settings,
    )

    Scaffold(
        topBar = {
            MainSearch(
                textFieldState = textFieldState,
                onSearch = { query ->
                    // 处理搜索逻辑
                    searchResults.value = listOf("结果1: $query", "结果2: $query")
                },
                searchResults = searchResults.value,
                modifier = Modifier.fillMaxWidth()
            )
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

                    composable(BottomNavScreen.Profile.route) { backStackEntry ->
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainSearch(
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Box(
        modifier
            .fillMaxWidth()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                    onSearch = {
                        onSearch(textFieldState.text.toString())
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text(text = "搜索") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            Column(Modifier.verticalScroll( rememberScrollState())) {
                searchResults.forEach{
                    ListItem(
                        headlineContent = {
                            Text(
                                text = "暂未搜索结果",
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        modifier = Modifier
                            .clickable {
                                textFieldState.edit { replace(0, length, it) }
                                expanded = false
                            }
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun ScreenContent(text: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text)
    }
}

@Preview(apiLevel = 34)
@Composable
fun MainScreenPreview() {
    val textFieldState = rememberTextFieldState()
    val searchResults = remember { listOf("示例结果1", "示例结果2") }
    MainSearch(
        textFieldState = textFieldState,
        onSearch = { query -> /* 处理搜索逻辑 */ },
        searchResults = searchResults
    )
}

