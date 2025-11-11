package com.atri.composemusic.core.data.di

import com.atri.composemusic.core.data.repository.LoginRepository
import com.atri.composemusic.core.data.repository.LoginRepositoryImpl
import com.atri.composemusic.core.data.repository.MusicPlayRepository
import com.atri.composemusic.core.data.repository.MusicPlayRepositoryImpl
import com.atri.composemusic.core.data.repository.PlayerListRepository
import com.atri.composemusic.core.data.repository.PlayerListRepositoryImpl
import com.atri.composemusic.core.data.repository.PlayerRepository
import com.atri.composemusic.core.data.repository.PlayerRepositoryImpl
import com.atri.composemusic.core.data.util.ConnectivityManagerNetworkMonitor
import com.atri.composemusic.core.data.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindLoginRepository(
        loginRepositoryImpl: LoginRepositoryImpl
    ): LoginRepository

    @Binds
    @Singleton
    abstract fun bindPlayerRepository(
        playerRepositoryImpl: PlayerRepositoryImpl
    ): PlayerRepository

    @Binds
    @Singleton
    abstract fun bindsPlayerListRepository(
        playerListRepositoryImpl: PlayerListRepositoryImpl
    ): PlayerListRepository

    @Binds
    @Singleton
    abstract fun bindsMusicPlayRepository(
        musicPlayRepositoryImpl: MusicPlayRepositoryImpl
    ) : MusicPlayRepository

    @Binds
    @Singleton
    abstract fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor
    ): NetworkMonitor
}