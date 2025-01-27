package com.samraa.vizztablet.utils

import androidx.appcompat.app.AppCompatDelegate
import com.samraa.data.enums.Theme

class ThemeChanger {

    operator  fun invoke(theme: Theme) = change(theme)

    private fun change(theme: Theme) {
        when (theme) {
            Theme.FOLLOW_SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            Theme.DARK_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Theme.LIGHT_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}