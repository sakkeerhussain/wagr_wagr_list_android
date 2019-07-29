package com.example.walklist.views.adapters


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.walklist.R
import com.example.walklist.utils.MapUtils
import com.example.walklist.utils.Walk
import com.example.walklist.views.fragments.WalkListFragment.ListInteractionListener
import com.google.android.gms.maps.GoogleMap
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

        private var mMap: GoogleMap? = null

        fun bind(position: Int) {
            val walk = mWalks[position]
            this.mView.tvTitle.text = walk.title
            this.mView.description.text = walk.description()

            // It's not a good practice to add map in recycler view.
            // Consider updating the UX in the future to avoid map from list.
            this.mView.mvWalk.onCreate(null)
            this.mView.mvWalk.onResume()
            this.mView.mvWalk.getMapAsync {
                this.mMap = it
                MapUtils.updateMapCamera(it, walk.startPointLat, walk.startPointLong)
                MapUtils.drawRouteLine(it, walk.encodedRoute, Color.BLUE)
                MapUtils.createMarker(it, walk.startPointLat, walk.startPointLong, "Start")
                MapUtils.createMarker(it, walk.endPointLat!!, walk.endPointLong!!, "End")
            }

            with(mView) {
                tag = walk
                setOnClickListener { v ->
                    val walk = v.tag as Walk
                    mListener?.onWalkClicked(walk, mView.mvWalk)
                }
            }
        }
    }
}
