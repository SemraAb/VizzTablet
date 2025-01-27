package com.samraa.vizztablet.utils

import android.content.Context
import android.content.ContextWrapper
import android.os.LocaleList
import java.util.*

class LocaleWrapper(base: Context) : ContextWrapper(base) {

    companion object {
        fun updateLocale(c: Context, localeToSwitchTo: Locale): ContextWrapper {
            var context = c
            val resources = context.resources
            val configuration = resources.configuration
            val localeList = LocaleList(localeToSwitchTo)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)

            context = context.createConfigurationContext(configuration)
            return LocaleWrapper(context)
        }
    }
}