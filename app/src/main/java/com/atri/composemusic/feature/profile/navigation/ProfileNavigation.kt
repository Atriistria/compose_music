package com.atri.composemusic.feature.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.atri.composemusic.feature.profile.ProfileScreen
import kotlinx.serialization.Serializable

@Serializable data object ProfileRoute

@Serializable data object ProfileBaseRoute

fun NavController.navigateToProfile(navOptions: NavOptions? = null) = navigate(route = ProfileRoute, navOptions)

fun NavGraphBuilder.profileScreen(
    navigateToProfile: () -> Unit
) {
    navigation<ProfileBaseRoute>(startDestination = ProfileRoute) {
        composable<ProfileRoute> {
            ProfileScreen(navigateToProfile)
        }
    }
}