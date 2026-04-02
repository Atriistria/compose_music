package com.atri.composemusic.feature.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable data object SearchRoute

@Serializable data object SearchBaseRoute

fun NavController.navigateToSearch(navOptions: NavOptions) = navigate(route = SearchRoute, navOptions)

fun NavGraphBuilder.searchScreen(

) {
    navigation<SearchBaseRoute>(startDestination = SearchRoute) {
        composable<SearchRoute> {

        }
    }
}
