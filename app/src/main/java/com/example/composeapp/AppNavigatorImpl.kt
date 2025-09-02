package com.example.composeapp

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class AppNavigatorImpl: AppNavigator {
    private val _navigationEvent = MutableSharedFlow<NavigationEvent>(extraBufferCapacity = 1)
    override val navigationEvent: SharedFlow<NavigationEvent>
        get() = _navigationEvent

    override fun navigateTo(route: String) {
        _navigationEvent.tryEmit(NavigationEvent.Navigate(route))
    }

    override fun navigateBack() {
        _navigationEvent.tryEmit(NavigationEvent.NavigateBack)
    }

    override fun navigateUp() {
        _navigationEvent.tryEmit(NavigationEvent.NavigateUp)
    }
}