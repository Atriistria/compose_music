package com.atri.composemusic.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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

    private var navJob: Job? = null
    private val navDebounceTime = 300L

    override fun navigateTo(route: String) {
        navJob?.cancel()
        navJob = viewModelScope.launch {
            delay(navDebounceTime)
            _navigationEvent.emit(NavigationEvent.Navigate(route))
        }
    }

    override fun navigateBack() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateBack)
        }
    }

    override fun navigateUp() {
        navJob?.cancel()
        navJob = viewModelScope.launch {
            delay(navDebounceTime)
            _navigationEvent.emit(NavigationEvent.NavigateUp)
        }
    }
}