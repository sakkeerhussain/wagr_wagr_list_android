package com.example.walklist.views.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.walklist.R


import com.example.walklist.views.fragments.WalkListFragment.ListInteractionListener
import com.example.walklist.utils.Walk

import kotlinx.android.synthetic.main.list_item_walk.view.*

/**
 * [RecyclerView.Adapter] that can display a [Walk] and makes a call to the
 * specified [ListInteractionListener].
 */
class WalkListAdapter(
    private val mValues: List<Walk>,
    private val mListener: ListInteractionListener?
) : RecyclerView.Adapter<WalkListAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Walk
            mListener?.onWalkClicked(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_walk, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        fun bind(position: Int) {
            val item = mValues.get(position)
            this.mView.title.text = item.title
            this.mView.description.text = item.content

            with(mView) {
                tag = item
                setOnClickListener(mOnClickListener)
            }
        }
    }
}
