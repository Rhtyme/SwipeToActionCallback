package com.rhtyme.swipetoaction

import androidx.recyclerview.widget.ItemTouchHelper

enum class SwipeDirection {
    TO_LEFT, TO_RIGHT, TO_LEFT_RIGHT
}

fun SwipeDirection.toMovementFlags(): Int {
    return when (this) {
        SwipeDirection.TO_LEFT -> {
            ItemTouchHelper.LEFT
        }
        SwipeDirection.TO_RIGHT -> {
            ItemTouchHelper.RIGHT
        }
        SwipeDirection.TO_LEFT_RIGHT -> {
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        }
    }
}

fun SwipeDirection.canSwipeToLeft(): Boolean {
    return this == SwipeDirection.TO_LEFT_RIGHT || this == SwipeDirection.TO_LEFT
}

fun SwipeDirection.canSwipeToRight(): Boolean {
    return this == SwipeDirection.TO_LEFT_RIGHT || this == SwipeDirection.TO_LEFT
}