package com.example.composeapp

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy.Builder
import com.example.composeapp.util.MToast
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        setStrictModePolicy()
        MToast.init(this)
    }

    private fun isDebuggable(): Boolean {
        return 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    }

    private fun setStrictModePolicy() {
        if (isDebuggable()) {
            StrictMode.setThreadPolicy(
                Builder().detectAll().penaltyLog().build(),
            )
        }
    }

}