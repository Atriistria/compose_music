package com.atri.composemusic.feature.myfavourite.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.atri.composemusic.feature.myfavourite.MyFavouriteScreen
import kotlinx.serialization.Serializable

@Serializable data object MyFavouriteRoute

@Serializable data object MyFavouriteBaseRoute

fun NavController.navigateToMyFavourite(navOptions: NavOptions? = null) = navigate(MyFavouriteRoute, navOptions)

fun NavGraphBuilder.myFavouriteScreen() {
    navigation<MyFavouriteBaseRoute>(startDestination = MyFavouriteRoute) {
        composable<MyFavouriteRoute> {
            MyFavouriteScreen()
        }
    }
}