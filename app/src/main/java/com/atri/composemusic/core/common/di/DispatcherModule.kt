package com.atri.composemusic.core.common.di

import com.atri.composemusic.core.common.Dispatcher
import com.atri.composemusic.core.common.CmDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Provides
    @Dispatcher(CmDispatcher.IO)
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Dispatcher(CmDispatcher.Default)
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Dispatcher(CmDispatcher.Main)
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}