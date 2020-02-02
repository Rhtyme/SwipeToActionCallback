package com.rhtyme.swipetoaction

import android.content.Context
import androidx.core.content.ContextCompat

object DataUtils {

    fun provideTestData(): List<Contact> {
        val list = arrayListOf<Contact>()
        for (i in 0..50) {
            val contact = Contact("Contact + $i")
            list.add(contact)
        }
        return list
    }

    fun defaultSwipeConfig(context: Context): SwipeConfig {
        return SwipeConfig(
            swipeDirection = SwipeDirection.TO_LEFT_RIGHT,
            leftIcon = context.getDrawableExt(R.drawable.ic_action_favourite),
            rightIcon = context.getDrawableExt(R.drawable.ic_action_like),
            leftActiveBackColor = context.getColorExt(R.color.left_active_color),
            rightActiveBackColor = context.getColorExt(R.color.right_active_color),
            disableBackColor = context.getColorExt(R.color.gray_back),
            leftText = context.getString(R.string.favourite),
            rightText = context.getString(R.string.like)
        )
    }
}