package com.example.composeapp.util

import android.annotation.SuppressLint
import android.util.Log
import com.example.composeapp.BuildConfig
import java.io.FileNotFoundException
import java.net.SocketTimeoutException
import java.util.Locale

object MLog {

    @SuppressLint("ConstantLocale")
    private val dateFormat = java.text.SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())

    @JvmStatic
    fun d(msg: String) {
        log(Log.DEBUG, getCallerClassName(), msg)
    }

    @JvmStatic
    fun d(tag: String, msg: String) {
        log(Log.DEBUG, tag, msg)
    }

    @JvmStatic
    fun i(msg: String) {
        log(Log.INFO, getCallerClassName(), msg)
    }

    @JvmStatic
    fun i(tag: String, msg: String) {
        log(Log.INFO, tag, msg)
    }

    @JvmStatic
    fun w(msg: String) {
        log(Log.WARN, getCallerClassName(), msg)
    }

    @JvmStatic
    fun w(tag: String,msg: String) {
        log(Log.WARN, tag, msg)
    }

    @JvmStatic
    fun w(ex: Throwable, module: String = "GENERIC") {
        val tag = getCallerClassName()
        val message = "[模块: $module] ${ex.javaClass.simpleName}: ${ex.message}\n${Log.getStackTraceString(ex)}"
        log(Log.WARN, tag, message)
    }

    @JvmStatic
    fun e(msg: String) {
        log(Log.ERROR, getCallerClassName(), msg)
    }

    @JvmStatic
    fun e(tag: String, msg: String) {
        log(Log.ERROR, tag, msg)
    }

    @JvmStatic
    fun e(ex: Throwable, module: String = "GENERIC") {
        val tag = getCallerClassName()
        val message = when (ex) {
            is FileNotFoundException -> {
                val path = ex.message ?: "unknown"
                val file = java.io.File(path)
                """
                [模块: $module] 文件未找到异常
                路径: $path
                存在: ${file.exists()} | 可读: ${file.canRead()}
                剩余空间: ${getDiskSpace()} GB
                """.trimIndent()
            }

            is SocketTimeoutException -> {
                """
                [模块: $module] 网络请求超时
                线程: ${Thread.currentThread().name}(${Thread.currentThread().id})
                网络类型: ${getNetworkType()}
                内存: ${getUsedMemory()} MB
                """.trimIndent()
            }

            is NullPointerException -> {
                """
                [模块: $module] 空指针异常
                异常信息: ${ex.message}                
                堆栈跟踪: ${Log.getStackTraceString(ex)}                
                """.trimIndent()
            }

            is IllegalArgumentException -> {
                """
                [模块: $module] 非法参数异常
                异常信息: ${ex.message}
                堆栈跟踪: ${Log.getStackTraceString(ex)}
                """.trimIndent()
            }

            else -> {
                "[模块: $module] ${ex.javaClass.simpleName}: ${ex.message}\n${Log.getStackTraceString(ex)}"
            }
        }
        log(Log.ERROR, tag, message)
    }

    private fun log(priority: Int, tag: String, msg: String) {
        if (!BuildConfig.DEBUG) return
        val time = dateFormat.format(java.util.Date())
        Log.println(priority, tag, "$time $msg")
    }

    private fun getCallerClassName(): String {
        val stackTrace = Throwable().stackTrace
        for (i in stackTrace.indices) {
            val ste = stackTrace[i]
            if (!ste.className.contains("mLog")) {
                val simpleClassName = ste.className.substringAfterLast(".")

                return simpleClassName.substringBefore("$")
            }
        }
        return "Unknown"
    }

    private fun getDiskSpace(): Long {
        val stat = android.os.StatFs(android.os.Environment.getDataDirectory().path)
        return stat.availableBlocksLong * stat.blockSizeLong / (1024 * 1024 * 1024) // GB
    }

    private fun getUsedMemory(): Long {
        val runtime = Runtime.getRuntime()
        val usedMem = runtime.totalMemory() - runtime.freeMemory()
        return usedMem / (1024 * 1024) // MB
    }

    private fun getNetworkType(): String {
        return "Wi-Fi"
    }
}
