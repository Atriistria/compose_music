package com.example.composeapp

import android.app.Application
import com.example.composeapp.util.MToast
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        MToast.init(this)
    }

}