package com.atri.composemusic.core.data.util

import kotlinx.coroutines.flow.Flow

fun interface NetworkMonitor {
    fun isOnline(): Flow<Boolean>
}