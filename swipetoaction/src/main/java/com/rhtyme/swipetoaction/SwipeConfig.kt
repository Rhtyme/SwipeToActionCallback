package com.rhtyme.swipetoaction

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt

data class SwipeConfig(
    var swipeDirection: SwipeDirection,
    var leftIcon: Drawable? = null,
    var rightIcon: Drawable? = null,
    @ColorInt var leftActiveBackColor: Int? = null,
    @ColorInt var rightActiveBackColor: Int? = null,
    @ColorInt var disableBackColor: Int = Color.GRAY,
    @ColorInt var textColor: Int = Color.WHITE,
    var textSizeSp: Float = 14f,
    var leftText: String? = null,
    var rightText: String? = null
)