package com.atri.composemusic.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.atri.composemusic.R
import com.atri.composemusic.feature.home.HomeBaseRoute
import com.atri.composemusic.feature.home.HomeRoute
import com.atri.composemusic.feature.myfavourite.navigation.MyFavouriteBaseRoute
import com.atri.composemusic.feature.myfavourite.navigation.MyFavouriteRoute
import com.atri.composemusic.feature.profile.navigation.ProfileBaseRoute
import com.atri.composemusic.feature.profile.navigation.ProfileRoute
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route,
) {
    HOME(
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Default.Home,
        iconTextId = R.string.home_title,
        titleTextId = 0,
        route = HomeRoute::class,
        baseRoute = HomeBaseRoute::class
    ),
    PROFILE(
        selectedIcon = Icons.Default.Person,
        unselectedIcon = Icons.Default.Person,
        iconTextId = R.string.profile_title,
        titleTextId = 0,
        route = ProfileRoute::class,
        baseRoute = ProfileBaseRoute::class
    ),
    MyFavourite(
        selectedIcon = Icons.Default.Favorite,
        unselectedIcon = Icons.Default.FavoriteBorder,
        iconTextId = R.string.profile_title,
        titleTextId = 0,
        route = MyFavouriteRoute::class,
        baseRoute = MyFavouriteBaseRoute::class
    )
}