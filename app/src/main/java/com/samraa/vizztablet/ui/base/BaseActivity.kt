package com.samraa.vizztablet.ui.base

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import com.samraa.data.enums.LanguageEnum.Companion.lowercase
import com.samraa.data.persistence.SessionManager
import com.samraa.vizztablet.utils.LocaleWrapper
import com.samraa.vizztablet.utils.extension.hideSoftKeyboard
import java.util.Locale

abstract class BaseActivity: AppCompatActivity() {
    override fun onSupportNavigateUp(): Boolean {
        hideSoftKeyboard()

        onBackPressed()
        return true
    }

    override fun attachBaseContext(newBase: Context) {
        val localeToSwitchTo = Locale(SessionManager.language.lowercase())
        val localeUpdatedContext: ContextWrapper =
            LocaleWrapper.updateLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }
}