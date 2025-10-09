package com.example.composeapp.core.common.profiler

import android.util.Log
import android.view.Choreographer

object FrameRateMonitor {

    private val choreographer = Choreographer.getInstance()
    private var frameCount = 0
    private var startTime = 0L
    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            if (startTime == 0L) {
                startTime = frameTimeNanos
            }
            frameCount++

            if (frameTimeNanos - startTime > 1_000_000_000L) {
                val fps = frameCount.toDouble() / ((frameTimeNanos - startTime) / 1_000_000_000.0)
                Log.d("FPS: ",fps.toString())
                if (fps < 55) {
                    Log.w("FPS: ",fps.toString())
                }
                frameCount = 0
                startTime = frameTimeNanos
            }
            choreographer.postFrameCallback(this)
        }
    }

    @JvmStatic
    fun start() {
        choreographer.postFrameCallback(frameCallback)
    }

    @JvmStatic
    fun stop() {
        choreographer.removeFrameCallback(frameCallback)
    }
}