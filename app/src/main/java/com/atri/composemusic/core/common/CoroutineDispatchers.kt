package com.atri.composemusic.core.common

import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Dispatcher(val type: DispatcherType)

enum class DispatcherType { IO, Default, Main }