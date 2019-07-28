package com.example.walklist.views.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.walklist.R
import com.example.walklist.utils.Walk
import com.example.walklist.views.fragments.WalkListFragment.ListInteractionListener
import kotlinx.android.synthetic.main.list_item_walk.view.*

/**
 * [RecyclerView.Adapter] that can display a [Walk] and makes a call to the
 * specified [ListInteractionListener].
 */
class WalkListAdapter(private val mListener: ListInteractionListener?) : RecyclerView.Adapter<WalkListAdapter.ViewHolder>() {

    private var mWalks: List<Walk> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_walk, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return mWalks.size
    }

    fun setData(walks: List<Walk>) {
        mWalks = walks
        notifyDataSetChanged()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        fun bind(position: Int) {
            val item = mWalks[position]
            this.mView.tvTitle.text = item.title
            this.mView.description.text = item.description()

            with(mView) {
                tag = item
                setOnClickListener { v ->
                    val walk = v.tag as Walk
                    mListener?.onWalkClicked(walk, mView.mvWalk)
                }
            }
        }
    }
}
