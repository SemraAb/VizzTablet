package com.samraa.vizztablet.utils.extension

import android.content.res.Resources
import android.util.TypedValue


fun Int.dpToPx(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()
}

fun Int.spToPx(): Int {
    val fontScale = Resources.getSystem().displayMetrics.scaledDensity
    return (this * fontScale + 0.5f).toInt()
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()


