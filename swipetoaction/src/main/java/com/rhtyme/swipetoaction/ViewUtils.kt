package com.rhtyme.swipetoaction

import android.content.Context
import kotlin.math.ceil

object ViewUtils {
    private var density = 1f

    fun fetchDp(value: Float, context: Context): Int {
        if (density == 1f) {
            checkDisplaySize(context)
        }
        return if (value == 0f) {
            0
        } else ceil((density * value).toDouble()).toInt()
    }

    private fun checkDisplaySize(context: Context) {
        try {
            density = context.resources.displayMetrics.density
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}