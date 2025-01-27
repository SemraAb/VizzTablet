package com.samraa.vizztablet.utils.extension

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

fun View?.hideSoftKeyboard() {
    this?.let {
        val inputMethodManager =
            ContextCompat.getSystemService(it.context, InputMethodManager::class.java)
        inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
    }
}