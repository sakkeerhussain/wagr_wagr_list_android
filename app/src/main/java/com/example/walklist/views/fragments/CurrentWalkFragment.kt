package com.example.walklist.views.fragments

import android.content.Context
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.walklist.R
import com.example.walklist.controllers.BaseController
import com.example.walklist.controllers.WalkController
import kotlinx.android.synthetic.main.fragment_current_walk.*
import kotlinx.android.synthetic.main.fragment_current_walk.view.*
import java.util.*

class CurrentWalkFragment : Fragment(), BaseController.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_current_walk, container, false)
        setupViews()
        setListeners(view)
        WalkController.refreshActiveWalkFromRemote(view.context)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        WalkController.addListener(this)
    }

    override fun onDetach() {
        super.onDetach()
        WalkController.removeListener(this)
    }


    private fun setupViews() {
        val walk = WalkController.mActiveWalk

        if (walk == null) {
            root?.visibility = View.GONE
        } else {
            root?.visibility = View.VISIBLE
            tvTitle?.text = walk.title
            tvDistance?.text = "${walk.distanceKM()} KM"
            tvDuration?.text = "${walk.duration} mins"
        }
    }

    private fun setListeners(view: View) {
        view.btEndWalk.setOnClickListener {
            val activeWalk = WalkController.mActiveWalk ?: return@setOnClickListener
            // TODO - Update to the actual current point
            val currentPointLat = 11.1111
            val currentPointLong = 71.1111

            activeWalk.endPointLat = currentPointLat
            activeWalk.endPointLong = currentPointLong
            activeWalk.duration += (activeWalk.resumedAt.time - Date().time) / 1000 % 60

            val result = floatArrayOf()
            Location.distanceBetween(activeWalk.startPointLat, activeWalk.startPointLong, activeWalk.endPointLat!!, activeWalk.endPointLong!!, result)
            activeWalk.distance += result[0]
            WalkController.mActiveWalk  = activeWalk

            WalkController.endCurrentWalk(it.context)
        }
    }

    override fun dataChanged(sender: BaseController, type: Int) {

        activity?.runOnUiThread {
            if (sender is WalkController && sender.isOfType(type, WalkController.DATA_TYPE_ACTIVE_WALK)) {
                setupViews()
            }
        }
    }
}
