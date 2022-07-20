package com.crystal.timeisgold.utils

import android.os.Build
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatDelegate

object ThemeManager {

    fun applyTheme(@NonNull themePref: String) {
        when (themePref) {
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//                } else {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
//                }
        }
    }
}