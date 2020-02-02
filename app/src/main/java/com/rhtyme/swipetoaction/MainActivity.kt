package com.rhtyme.swipetoaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class MainActivity : AppCompatActivity(), SwipeListener {

    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag(RecyclerAdapter.RECYCLER_VIEW).d("initViews")
        setContentView(R.layout.activity_main)
        Timber.tag(RecyclerAdapter.RECYCLER_VIEW).d("initViews")
        initViews()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = RecyclerAdapter()
        Timber.tag(RecyclerAdapter.RECYCLER_VIEW).d("initViews")

        (recyclerView.adapter as RecyclerAdapter).setDataset(DataUtils.provideTestData())
        val swipeActionCallback = SwipeToActionsCallback(
            DataUtils.defaultSwipeConfig(this), this
        )
        val itemTouchHelper = ItemTouchHelper(swipeActionCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onSwipeFromLeftToRight(position: Int) {
        recyclerView.handler?.postDelayed({
            val item = (recyclerView.adapter as? RecyclerAdapter)?.myDataset?.getOrNull(position)
            item?.let {
                it.liked = !it.liked
                recyclerView.adapter?.notifyItemChanged(position)
            }
        }, ACTION_DELAY)
    }

    override fun onSwipeFromRightToLeft(position: Int) {
        recyclerView.handler?.postDelayed({
            val item = (recyclerView.adapter as? RecyclerAdapter)?.myDataset?.getOrNull(position)
            item?.let {
                it.favourited = !it.favourited
                recyclerView.adapter?.notifyItemChanged(position)
            }
        }, ACTION_DELAY)
    }

    companion object {
        const val MAIN_VIEW = "RECYCLER_VIEW"
        const val ACTION_DELAY = 500L
    }

}
