package com.samraa.vizztablet.utils.bindingAdapters

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.samraa.vizztablet.R
import com.samraa.vizztablet.utils.OnSingleClickListener
import com.samraa.vizztablet.utils.extension.dpToPx

@BindingAdapter("onSingleClick")
fun View.setOnSingleClickListener(clickListener: View.OnClickListener?) {
    clickListener?.also {
        setOnClickListener(OnSingleClickListener(it))
    } ?: setOnClickListener(null)
}


@BindingAdapter("isVisibleElseGone")
fun View.setVisibleElseGone(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("constraintEndToStartOfDynamic")
fun bindConstraintEndToStartOf(view: View, targetViewId: Int?) {
    if (targetViewId == null) return

    val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
    layoutParams.endToStart = targetViewId
    view.layoutParams = layoutParams
}

@BindingAdapter("bitmapSrc")
fun setBitmap(imageView: ImageView, bitmap: Bitmap?) {
    bitmap?.let {
        imageView.setImageBitmap(it)
    }
}
@BindingAdapter("setAlpha")
fun setAlpha(view: View, isLocked: Boolean) {
    view.alpha = if (isLocked) 0.5f else 1f
}