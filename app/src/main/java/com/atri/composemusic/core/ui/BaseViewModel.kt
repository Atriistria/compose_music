package com.atri.composemusic.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface UiState
interface UiIntent
interface UiEffect

abstract class BaseViewModel<S: UiState, I: UiIntent, E: UiEffect>(initialState: S): ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> get() = _uiState

    private val _uiEffect = MutableSharedFlow<E>()
    val uiEffect: SharedFlow<E> get() = _uiEffect

    abstract fun onIntent(intent: I)

    fun dispatch(intent: I) = onIntent(intent)

    protected fun setState(reduce: (S) -> S) {
        _uiState.update{ reduce(it) }
    }

    protected fun emitEffect(effect: E) {
        viewModelScope.launch {
            _uiEffect.emit(effect)
        }
    }
}