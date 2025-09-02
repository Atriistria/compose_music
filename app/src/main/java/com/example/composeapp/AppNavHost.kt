package com.example.composeapp

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeapp.ui.screen.LoginScreen
import com.example.composeapp.ui.screen.MainScreen
import com.example.composeapp.ui.screen.MyFavor
import com.example.composeapp.ui.screen.ProfileScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val navigationViewModel: NavigationViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        navigationViewModel.navigationEvent.collect { event ->
            when(event) {
                is NavigationEvent.Navigate -> navController.navigate(event.route)
                is NavigationEvent.NavigateUp -> navController.navigateUp()
                is NavigationEvent.NavigateBack -> navController.navigateUp()
            }
        }
    }

    NavHost(navController, Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen {
                navigationViewModel.navigateTo(Screen.Home.route)
            }
        }

        composable(Screen.Home.route) { MainScreen(navigationViewModel) }

        composable(Screen.MyFavor.route) { MyFavor() }

    }
}