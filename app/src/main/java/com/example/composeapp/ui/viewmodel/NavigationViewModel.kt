package com.example.composeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.AppNavigator
import com.example.composeapp.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor() : ViewModel(), AppNavigator {
    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    override val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    override fun navigateTo(route: String) {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.Navigate(route))
        }
    }

    override fun navigateBack() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateBack)
        }
    }

    override fun navigateUp() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateUp)
        }
    }
}