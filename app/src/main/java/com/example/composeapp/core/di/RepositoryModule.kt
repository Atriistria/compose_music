package com.example.composeapp.core.di

import com.example.composeapp.core.data.repository.LoginRepository
import com.example.composeapp.core.data.repository.LoginRepositoryImpl
import com.example.composeapp.core.data.repository.PlayerRepository
import com.example.composeapp.core.data.repository.PlayerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindLoginRepository(
        impl: LoginRepositoryImpl
    ): LoginRepository

    @Binds
    @Singleton
    abstract fun bindPlayerRepository(
        playerRepositoryImpl: PlayerRepositoryImpl
    ): PlayerRepository
}