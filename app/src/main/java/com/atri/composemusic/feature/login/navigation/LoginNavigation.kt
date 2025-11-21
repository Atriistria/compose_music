package com.atri.composemusic.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.atri.composemusic.feature.login.LoginScreen
import kotlinx.serialization.Serializable

@Serializable data object LoginRoute

@Serializable data object LoginBaseRoute

fun NavController.navigateToLogin(navOptions: NavOptions) = navigate(route = LoginRoute, navOptions)

fun NavGraphBuilder.loginScreen(
    onLoginSucceed: () -> Unit,
    topicDestination: NavGraphBuilder.() -> Unit
) {
    navigation<LoginBaseRoute>(startDestination = LoginRoute) {
        composable<LoginRoute> {
            LoginScreen(onLoginSucceed = onLoginSucceed)
        }
        topicDestination()
    }
}
