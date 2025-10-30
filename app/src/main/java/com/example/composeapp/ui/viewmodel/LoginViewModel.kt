package com.example.composeapp.ui.viewmodel

import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.viewModelScope
import com.example.composeapp.core.data.repository.AppPreferencesRepository
import com.example.composeapp.core.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
): UiState

sealed interface LoginIntent: UiIntent {
    data class UpdateUsername(val username: String): LoginIntent
    data class UpdatePassword(val password: String): LoginIntent
    data object Login: LoginIntent
}

sealed interface LoginEffect: UiEffect {
    data class ShowToast(val message: String): LoginEffect
    data object NavigateHome: LoginEffect
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val appPreferences: AppPreferencesRepository
): BaseViewModel<LoginUiState, LoginIntent, LoginEffect>(LoginUiState()) {

    override fun onIntent(intent: LoginIntent) {
        when(intent) {
            is LoginIntent.UpdateUsername -> setState { it.copy(username = intent.username) }
            is LoginIntent.UpdatePassword -> setState { it.copy(password = intent.password) }
            is LoginIntent.Login -> login()
        }
    }

    private fun login() {
        val state = uiState.value
        if (state.username.isBlank() || state.password.isBlank()) {
            emitEffect(LoginEffect.ShowToast("用户名或密码不能为空"))
            return
        }
        setState { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = repository.login(state.username, state.password)
            Log.d("LoginViewModel", "result: $result")
            if (result.isSuccess) {
                emitEffect(LoginEffect.NavigateHome)
                emitEffect(LoginEffect.ShowToast("登录成功"))
            } else {
                setState { it.copy(isLoading = false, error = "登录失败") }
                emitEffect(LoginEffect.ShowToast("用户名或密码错误"))
                emitEffect(LoginEffect.NavigateHome)
            }
            appPreferences.put(booleanPreferencesKey("is_login"), true)
        }
    }

}