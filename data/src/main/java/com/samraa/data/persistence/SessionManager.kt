package com.samraa.data.persistence

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaFormat.KEY_LANGUAGE
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.samraa.data.enums.LanguageEnum
import com.samraa.data.enums.Theme

object SessionManager {
    private lateinit var sharedPref: SharedPreferences

    private const val KEY_THEME_NAME = "theme_name"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_LOGIN = "logged_in"
    private const val KEY_TOKEN = "access_token"
    private const val KEY_FIREBASE_TOKEN = "firebase_token"


    fun init(context: Context) {
        sharedPref = EncryptedSharedPreferences.create(
            context, "secret_shared_preferences",
            createMasterKey(context),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun createMasterKey(context: Context) =
        MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()


    var firebaseToken
        get() = sharedPref.getString(KEY_FIREBASE_TOKEN, "") ?: ""
        set(value) {
            sharedPref.edit { putString(KEY_FIREBASE_TOKEN, value) }
        }

    var token
        get() = "Bearer ${sharedPref.getString(KEY_TOKEN, "") ?: ""}"
        set(value) {
            sharedPref.edit { putString(KEY_TOKEN, value) }
        }

    val appTheme: Theme
        get() {
            val theme = sharedPref.getString(KEY_THEME_NAME, Theme.FOLLOW_SYSTEM.name)
            return Theme.valueOf(theme ?: Theme.FOLLOW_SYSTEM.name)
        }

    var language
        get() = LanguageEnum.find(sharedPref.getString(KEY_LANGUAGE, LanguageEnum.AZ.name))
        set(value) {
            sharedPref.edit { putString(KEY_LANGUAGE, value.name) }
        }

    var userName
        get() = sharedPref.getString(KEY_USER_NAME, "User")
        set(value) {
            sharedPref.edit { putString(KEY_USER_NAME, value) }
        }

    var loggedIn
        get() = sharedPref.getBoolean(KEY_LOGIN, false)
        set(value) {
            sharedPref.edit { putBoolean(KEY_LOGIN, value) }
        }
}