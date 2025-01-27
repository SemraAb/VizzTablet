package com.samraa.vizztablet.core

import android.content.Context
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.samraa.data.di.appModuleApi
import com.samraa.data.di.appModuleRepo
import com.samraa.data.persistence.SessionManager
import com.samraa.vizztablet.di.appModule
import com.samraa.vizztablet.utils.ThemeChanger
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class VizzTabletApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        init()
        initKoin()
        initTheme()
    }

    private fun init() {
        SessionManager.init(applicationContext)
        FirebaseApp.initializeApp(this)

        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())


    }

    private fun initKoin() {
        startKoin {
            allowOverride(true)
            androidContext(this@VizzTabletApp)
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidFileProperties()
            modules(listOf(appModule, appModuleRepo, appModuleApi))

        }
    }

    private fun initTheme(){
        ThemeChanger().invoke(SessionManager.appTheme)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}