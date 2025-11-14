package com.atri.composemusic.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor() : ViewModel(), AppNavigator {
    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    override val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    private val navDebounceTime = 500L
    private var isNavigating = false

    override fun navigateTo(route: String) {
        if (isNavigating) return
         viewModelScope.launch {
             isNavigating = true
            _navigationEvent.emit(NavigationEvent.Navigate(route))
             delay(navDebounceTime)
             isNavigating = false
        }
    }

    override fun navigateBack() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateBack)
        }
    }

    override fun navigateUp() {
        viewModelScope.launch {
            delay(navDebounceTime)
            _navigationEvent.emit(NavigationEvent.NavigateUp)
        }
    }
}