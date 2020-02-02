package com.rhtyme.swipetoaction

interface SwipeListener {

    fun onSwipeFromLeftToRight(position: Int)

    fun onSwipeFromRightToLeft(position: Int)
}