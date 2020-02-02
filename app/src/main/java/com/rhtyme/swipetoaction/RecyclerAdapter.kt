package com.rhtyme.swipetoaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_view.view.*
import timber.log.Timber

class RecyclerAdapter :
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    val myDataset: MutableList<Contact> = arrayListOf()

    fun setDataset(list: List<Contact>) {
        myDataset.clear()
        myDataset.addAll(list)
        notifyDataSetChanged()
    }

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view), SwipeableViewHolder {

        val swipeConfig = DataUtils.defaultSwipeConfig(view.context)

        fun onBind(contact: Contact) {
            view.tvTitle.text = contact.name
            view.imgFavourite.isVisible = contact.favourited
            view.imgLike.isVisible = contact.liked

            if (contact.liked) {
                swipeConfig.leftIcon = view.getDrawableExt(R.drawable.ic_action_unlike)
                swipeConfig.leftText = view.getStringExt(R.string.dislike)
            } else {
                swipeConfig.leftIcon = view.getDrawableExt(R.drawable.ic_action_like)
                swipeConfig.leftText = view.getStringExt(R.string.like)
            }

            if (contact.favourited) {
                swipeConfig.rightIcon = view.getDrawableExt(R.drawable.ic_action_unfavourite)
                swipeConfig.rightText = view.getStringExt(R.string.unfavourite)
            } else {
                swipeConfig.rightIcon = view.getDrawableExt(R.drawable.ic_action_favourite)
                swipeConfig.rightText = view.getStringExt(R.string.favourite)
            }

        }

        override fun provideSwipeConfig(): SwipeConfig {
            return swipeConfig
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Timber.tag(RECYCLER_VIEW).d("onBindViewHolder: ${myDataset[position]}")
        holder.onBind(myDataset[position])
    }

    override fun getItemCount() = myDataset.size

    companion object {
        const val RECYCLER_VIEW = "RECYCLER_VIEW"
    }
}
