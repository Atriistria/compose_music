package com.atri.composemusic.core.common

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object LanguageUtils {

    // 切换到简体中文
    fun switchToChinese() {
        val appLocale = LocaleListCompat.forLanguageTags("zh-CN")
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    // 切换到英文
    fun switchToEnglish() {
        val appLocale = LocaleListCompat.forLanguageTags("en-US")
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    // 跟随系统
    fun followSystem() {
        val appLocale = LocaleListCompat.getEmptyLocaleList()
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}