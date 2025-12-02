package com.atri.composemusic.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.atri.composemusic.feature.settings.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable data object SettingsRoute

@Serializable data object SettingsBaseRoute

fun NavController.navigateToSettings(navOptions: NavOptions? = null) = navigate(SettingsRoute, navOptions)

fun NavGraphBuilder.settingsScreen() {
    navigation<SettingsBaseRoute>(startDestination = SettingsRoute) {
        composable<SettingsRoute> {
            SettingsScreen()
        }
    }
}