package com.atri.composemusic.feature.musicplay.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.atri.composemusic.feature.musicplay.MusicPlayerScreen
import kotlinx.serialization.Serializable


@Serializable data object MusicPlayRoute

@Serializable data object MusicPlayBaseRoute

fun NavController.navigateToMusicPlay(navOptions: NavOptions) = navigate(route = MusicPlayRoute, navOptions)

fun NavGraphBuilder.musicPlayScreen() {
    navigation<MusicPlayBaseRoute>(startDestination = MusicPlayRoute) {
        composable<MusicPlayRoute> {
            MusicPlayerScreen()
        }
    }
}

