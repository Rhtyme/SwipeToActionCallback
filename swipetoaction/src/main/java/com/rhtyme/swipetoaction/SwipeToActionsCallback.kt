package com.rhtyme.swipetoaction

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.TypedValue
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class SwipeToActionsCallback(
    var swipeConfig: SwipeConfig,
    val swipeListener: SwipeListener
) :
    ItemTouchHelper.SimpleCallback(0, swipeConfig.swipeDirection.toMovementFlags()) {

    private var dX = 0f

    private var lastAnimationTime: Long = 0
    private var iconScale = 0f
    private var swipeBack = false
    private var isVibrate = false

    var leftActiveBackColorDrawable: ColorDrawable = ColorDrawable(Color.WHITE)
    var rightActiveBackColorDrawable: ColorDrawable = ColorDrawable(Color.WHITE)
    var disableBackColorDrawable: ColorDrawable = ColorDrawable(Color.WHITE)

    private var configParsed = false

    private var textEdgeMargin: Int = 0
    val backgroundCornerOffset = 20

    private var startTracking = false

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {

        if (viewHolder is SwipeableViewHolder) {
            this.swipeConfig = viewHolder.provideSwipeConfig()
            parseConfig(viewHolder.itemView.context)
        } else if (!configParsed) {
            parseConfig(viewHolder.itemView.context)
        }

        textEdgeMargin = convertToDp(5, viewHolder.itemView)

        return ItemTouchHelper.Callback.makeMovementFlags(
            ItemTouchHelper.ACTION_STATE_IDLE,
            swipeConfig.swipeDirection.toMovementFlags()
        )
    }

    private fun parseConfig(context: Context) {
        configParsed = true
        textPaint.textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            swipeConfig.textSizeSp,
            context.resources.displayMetrics
        )
        textPaint.textAlign = Paint.Align.LEFT
        textPaint.color = swipeConfig.textColor
        textPaint.style = Paint.Style.FILL

        if (swipeConfig.swipeDirection == SwipeDirection.TO_LEFT_RIGHT
            || swipeConfig.swipeDirection == SwipeDirection.TO_LEFT
        ) {
            leftActiveBackColorDrawable =
                ColorDrawable(swipeConfig.leftActiveBackColor ?: Color.WHITE)
        }

        if (swipeConfig.swipeDirection == SwipeDirection.TO_LEFT_RIGHT
            || swipeConfig.swipeDirection == SwipeDirection.TO_RIGHT
        ) {
            rightActiveBackColorDrawable =
                ColorDrawable(swipeConfig.rightActiveBackColor ?: Color.WHITE)
        }
        disableBackColorDrawable = ColorDrawable(swipeConfig.disableBackColor ?: Color.WHITE)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        recyclerView.setOnTouchListener { _, event ->
            swipeBack =
                event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
            if (swipeBack &&
                abs(viewHolder.itemView.translationX) >= convertToDp(100, viewHolder.itemView)
            ) {
                if (viewHolder.itemView.translationX > 0) {
                    swipeListener.onSwipeFromLeftToRight(viewHolder.adapterPosition)
                } else {
                    swipeListener.onSwipeFromRightToLeft(viewHolder.adapterPosition)
                }
            }
            false
        }
    }


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        isVibrate = false
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = false
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }


    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            setTouchListener(recyclerView, viewHolder)
        }

        val diff = convertToDp(130, viewHolder.itemView)

        if (dX > 0 && swipeConfig.swipeDirection.canSwipeToRight()) {
            if (abs(viewHolder.itemView.translationX) < diff || dX < this.dX) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                this.dX = dX
                startTracking = true
            }
            drawLeftLayout(c, viewHolder, dX)
        } else if (dX < 0 && swipeConfig.swipeDirection.canSwipeToLeft()) {
            if (abs(viewHolder.itemView.translationX) < diff || dX > this.dX) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                this.dX = dX
                startTracking = true
            }
            drawRightLayout(c, viewHolder, dX)
        }
/*
        else {
            leftActiveBackColorDrawable.setBounds(0, 0, 0, 0)
            rightBackColor.setBounds(0, 0, 0, 0)
            leftBackColor.draw(c)
            rightBackColor.draw(c)
        }
*/
    }

    private fun drawLeftLayout(
        c: Canvas,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float
    ) {

        val itemView = viewHolder.itemView
        val leftIcon =
            swipeConfig.leftIcon ?: throw Resources.NotFoundException("leftIcon not found!!!")

        val leftActiveBackColor = swipeConfig.leftActiveBackColor
            ?: throw Resources.NotFoundException("leftActiveBackColor not found!!!")

        val iconTopParam = if (swipeConfig.leftText.isNullOrEmpty()) {
            2
        } else {
            3
        }

        val iconTop: Int =
            itemView.top + (itemView.height - leftIcon.intrinsicHeight) / iconTopParam
        val iconBottom: Int = iconTop + leftIcon.intrinsicHeight

        var textX = 0
        var textY = 0
        var textWidth = 0

        var iconLeft: Int = leftIcon.intrinsicWidth / 2


        swipeConfig.leftText?.let {
            val textBounds = Rect()
            textPaint.getTextBounds(it, 0, it.length, textBounds)

            textWidth = textBounds.width()
            textX = itemView.left + textEdgeMargin
            textY = iconBottom + textEdgeMargin + textBounds.height()

            if (textWidth > leftIcon.intrinsicWidth) {
                iconLeft = (textX + ((textWidth - leftIcon.intrinsicWidth) / 2))
            }
        }


        val iconRight: Int = iconLeft + leftIcon.intrinsicWidth

        leftIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

        leftActiveBackColorDrawable.setBounds(
            itemView.left, itemView.top,
            itemView.left + dX.toInt() + backgroundCornerOffset,
            itemView.bottom
        )


        drawLayout(
            c, itemView, leftActiveBackColor,
            leftIcon, leftActiveBackColorDrawable,
            swipeConfig.leftText, textX, textY
        )

    }

    private fun drawRightLayout(
        c: Canvas,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float
    ) {
        val itemView = viewHolder.itemView

        val rightIcon =
            swipeConfig.rightIcon ?: throw Resources.NotFoundException("rightIcon not found!!!")

        val rightActiveBackColor = swipeConfig.rightActiveBackColor
            ?: throw Resources.NotFoundException("rightActiveBackColor not found!!!")

        val iconTopParam = if (swipeConfig.rightText.isNullOrEmpty()) {
            2
        } else {
            3
        }

        val iconTop: Int =
            itemView.top + (itemView.height - rightIcon.intrinsicHeight) / iconTopParam
        val iconBottom: Int = iconTop + rightIcon.intrinsicHeight


        //

        var textX = 0
        var textY = 0
        var textWidth = 0

        var iconLeft: Int = itemView.right - (rightIcon.intrinsicWidth * 3 / 2)

        swipeConfig.rightText?.let {
            val textBounds = Rect()
            textPaint.getTextBounds(it, 0, it.length, textBounds)

            textWidth = textBounds.width()
            textX = itemView.right - textWidth - textEdgeMargin
            textY = iconBottom + textEdgeMargin + textBounds.height()

            if (textWidth > rightIcon.intrinsicWidth) {
                iconLeft = (textX + ((textWidth - rightIcon.intrinsicWidth) / 2))
            }
        }


        val iconRight: Int = iconLeft + rightIcon.intrinsicWidth

        rightIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

        rightActiveBackColorDrawable.setBounds(
            itemView.right + dX.toInt() - backgroundCornerOffset,
            itemView.top, itemView.right, itemView.bottom
        )
        rightIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

        drawLayout(
            c, itemView, rightActiveBackColor,
            rightIcon, rightActiveBackColorDrawable, swipeConfig.rightText, textX, textY
        )
    }

    private fun drawLayout(
        c: Canvas,
        itemView: View,
        @ColorInt backGroundActiveColor: Int,
        icon: Drawable,
        background: ColorDrawable,
        text: String?,
        textX: Int,
        textY: Int
    ) {

        val translationX = itemView.translationX

        if (abs(translationX) >= activeTranslationThresholdDp(itemView)) {
            background.color = backGroundActiveColor
        } else {
            background.color = swipeConfig.disableBackColor
            isVibrate = false
        }

        val newTime = System.currentTimeMillis()

        if (startTracking) {
            if (!isVibrate && abs(itemView.translationX) >=
                activeTranslationThresholdDp(itemView)
            ) {
                itemView.performHapticFeedback(
                    HapticFeedbackConstants.KEYBOARD_TAP,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                )
                lastAnimationTime = newTime
                iconScale = 1.2f
                isVibrate = true
                itemView.postInvalidateDelayed(ICON_ANIMATION_TIME)
            }
        }

        if (abs(lastAnimationTime - newTime) >= ICON_ANIMATION_TIME) {
            iconScale = 0f
        }
        val scaleRatio = (convertToDp(4, itemView) * iconScale).toInt()
        icon.setBounds(
            icon.bounds.left - scaleRatio,
            icon.bounds.top - scaleRatio,
            icon.bounds.right + scaleRatio,
            icon.bounds.bottom + scaleRatio
        )

        background.draw(c)
        if (!text.isNullOrEmpty()) {
            c.drawText(text, textX.toFloat(), textY.toFloat(), textPaint)
        }

        icon.draw(c)
    }


    private fun convertToDp(pixel: Int, view: View): Int {
        return ViewUtils.fetchDp(pixel.toFloat(), view.context)
    }

    private fun activeTranslationThresholdDp(view: View): Int {
        return convertToDp(ACTIVE_TRANSLATION_THRESHOLD, view)
    }

    companion object {

        const val SWIPE_TAG = "SWIPE_TAG"

        const val ACTIVE_TRANSLATION_THRESHOLD = 100

        const val ICON_ANIMATION_TIME = 100L
    }
}