# Project Title

SwipeToActionCallback

## Description

This library allows you to implement "swipe to action" feature in your RecyclerView. This library
works with `RecyclerView`'s `ItemTouchHelper.SimpleCallback`, and draws icon and/or text under the icon.
The library allows you to have swipes from left, right or both sides at the same time. Look at the gif
that shows what can be achieved through this library:

![](screens/swipe_to_action.gif)

## Installation

Have a look the app module to understand how it works. The installation is quite simple.
The main class is `SwipeToActionsCallback` which inherits `ItemTouchHelper.SimpleCallback`.
The class `SwipeToActionsCallback` needs two parameters which needs to be passed:
1. `SwipeListener` implementation, through which you get notified about swipe actions.
2. The next parameter is `SwipeConfig`, which you decorate the left and/or 
right layouts when a recycler view item swiped.

Then you should attach the ItemTouchHelper to your RecyclerView. Example:

```kotlin        
        val swipeActionCallback = SwipeToActionsCallback(
            DataUtils.defaultSwipeConfig(this), this
        )
        val itemTouchHelper = ItemTouchHelper(swipeActionCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
```

## Config

The following is the description for the fields of `SwipeConfig`: 

- swipeDirection - enabled swipe direction, which might be TO_LEFT, 
TO_RIGHT or TO_LEFT_RIGHT (both) of SwipeDirection
- leftIcon - drawable icon, drawn on left, when swiped to right
- rightIcon - drawable icon, drawn on right, when swiped to left
- leftActiveBackColor - active background color of the layout,
 drawn on left, when swiped to right
- rightActiveBackColor - active background color of the layout,
 drawn on right, when swiped to left
- disableBackColor - inactive background color of the layout,
 drawn on right and/or left, when swiped to left and/or to right
- textColor - text color which is drawn under icon
- textSizeSp - text size in sp unit, which is drawn under icon 
- leftText - string text, drawn on left, when swiped to right
- rightText - string text, drawn on right, when swiped to left


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
