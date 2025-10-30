package com.example.composeapp

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.core.data.repository.AppPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val isLogin: Boolean) : MainActivityUiState
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val appPreferences: AppPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainActivityUiState>(MainActivityUiState.Loading)
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val isLogin = try {
                appPreferences.get(booleanPreferencesKey("is_login"), false).first()
            } catch (e: Exception) {
                false
            }
            _uiState.value = MainActivityUiState.Success(isLogin)
        }
    }
}