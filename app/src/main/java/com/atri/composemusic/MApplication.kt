package com.atri.composemusic

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy.Builder
import com.atri.composemusic.core.common.profiler.FrameRateMonitor
import com.atri.composemusic.util.MToast
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        setStrictModePolicy()
        MToast.init(this)
        FrameRateMonitor.start()
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