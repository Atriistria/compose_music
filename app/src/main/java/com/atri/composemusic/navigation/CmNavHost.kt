package com.atri.composemusic.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.atri.composemusic.feature.home.HomeBaseRoute
import com.atri.composemusic.feature.home.homeScreen
import com.atri.composemusic.feature.musicplay.navigation.musicPlayScreen
import com.atri.composemusic.feature.myfavourite.navigation.myFavouriteScreen
import com.atri.composemusic.feature.profile.navigation.profileScreen
import com.atri.composemusic.ui.CmAppState

@Composable
fun CmNavHost(
    appState: CmAppState,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = HomeBaseRoute,
        modifier = modifier
    ) {
        homeScreen()
        profileScreen {  }
        musicPlayScreen()
        myFavouriteScreen()
    }

}