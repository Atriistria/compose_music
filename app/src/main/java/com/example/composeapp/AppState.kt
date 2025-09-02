package com.example.composeapp

import kotlinx.coroutines.flow.SharedFlow

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object Profile : Screen("profile")
    data object Setting : Screen("setting")
    data object MusicPlayer : Screen("player")
    data object MyFavor : Screen("my_favor")
}

sealed interface NavigationEvent {
    data class Navigate(val route: String) : NavigationEvent
    data object NavigateBack : NavigationEvent
    data object NavigateUp : NavigationEvent
}

interface AppNavigator {
    val navigationEvent: SharedFlow<NavigationEvent>

    fun navigateTo(route: String)

    fun navigateBack()

    fun navigateUp()

}