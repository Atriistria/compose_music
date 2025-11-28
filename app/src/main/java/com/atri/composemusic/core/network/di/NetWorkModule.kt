package com.atri.composemusic.core.network.di

import android.content.Context
import coil.ImageLoader
import coil.util.DebugLogger
import com.atri.composemusic.BuildConfig
import com.atri.composemusic.core.network.retrofit.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideCallFactory(okHttpClient: OkHttpClient): Call.Factory {
        return okHttpClient
    }

    @Provides
    @Singleton
    fun provideUserApi(client: OkHttpClient): UserApi {
        return Retrofit.Builder()
            .baseUrl("https://www.baidu.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun imageLoader(
        // 使用 Lazy 是为了打破潜在的循环依赖，或者在不需要时延迟初始化
        okHttpCallFactory: dagger.Lazy<Call.Factory>,
        @ApplicationContext application: Context,
    ): ImageLoader {
        return ImageLoader.Builder(application)
            .callFactory { okHttpCallFactory.get() }
            .respectCacheHeaders(false)
            .apply {
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger())
                }
            }
            .build()
    }
}