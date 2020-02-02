package com.rhtyme.swipetoaction

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

fun View.getStringExt(@StringRes id: Int): String {
    return context.getString(id)
}

fun View.getDrawableExt(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(context, id)
}

fun Context.getDrawableExt(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(this, id)
}

@ColorInt fun Context.getColorExt(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}