package com.samraa.vizztablet.utils.extension

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHost
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent


fun AppCompatActivity.findNavHostNavController(id: Int): NavController {
    val navHost = supportFragmentManager.findFragmentById(id) as NavHost
    return navHost.navController
}


fun Activity.hideSoftKeyboard() {
    if (KeyboardVisibilityEvent.isKeyboardVisible(this))
        window.decorView.hideSoftKeyboard()
}



