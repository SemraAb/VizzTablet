package com.samraa.vizztablet.utils.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun View.isVisibleElseInVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}


@SuppressLint("ClickableViewAccessibility")
fun Activity.setupHideKeyboardOnOutsideClick(rootView: View) {
    rootView.setOnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            val currentFocus = currentFocus
            if (currentFocus is EditText) {
                val outBounds = IntArray(2)
                currentFocus.getLocationOnScreen(outBounds)
                val x = event.rawX
                val y = event.rawY

                // Check if the touch is outside the EditText
                if (x < outBounds[0] || x > outBounds[0] + currentFocus.width || y < outBounds[1] || y > outBounds[1] + currentFocus.height) {
                    // Clear focus and hide the keyboard
                    currentFocus.clearFocus()
                    hideKeyboard()
                }
            }
        }
        false
    }
}

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocus = currentFocus
    if (currentFocus != null) {
        inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}
