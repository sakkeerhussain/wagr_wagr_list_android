package com.example.walklist.views.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.walklist.R
import com.example.walklist.controllers.BaseController
import com.example.walklist.controllers.WalkController
import com.example.walklist.utils.MapUtils
import com.example.walklist.utils.Walk
import com.example.walklist.views.activities.BaseActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polyline
import kotlinx.android.synthetic.main.fragment_current_walk.view.*
import java.util.*
import kotlin.math.ceil

class CurrentWalkFragment : Fragment(), BaseController.Listener {

    private var mWalkRoute: Polyline? = null
    private var mLocationMarker: Marker? = null
    private var mStartMarker: Marker? = null
    private var mMap: GoogleMap? = null
    private var mBaseActivity: BaseActivity? = null
    var mView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_current_walk, container, false)
        setupViews()
        setListeners(mView!!)
        WalkController.refreshActiveWalkFromRemote(mView!!.context)


        mView!!.mapView.onCreate(savedInstanceState)
        mView!!.mapView.getMapAsync {
            this.mMap = it
        }

        return mView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mBaseActivity = context as BaseActivity
        WalkController.addListener(this)
    }

    override fun onDetach() {
        super.onDetach()
        mBaseActivity = null
        WalkController.removeListener(this)
    }

    override fun onStart() {
        super.onStart()
        mView?.mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mView?.mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mView?.mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        // WalkController.pauseCurrentWalk(mBaseActivity!!)
        mView?.mapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mView?.mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mView?.mapView?.onLowMemory()
    }


    private fun setupViews() {
        val view = mView ?: return
        val walk = WalkController.mActiveWalk

        if (walk == null) {
            view.flRoot.visibility = View.GONE
        } else {
            view.flRoot.visibility = View.VISIBLE
            view.tvTitle.text = walk.title
            view.tvDistance.text = walk.distanceStr()
            var duration = walk.duration
            if (!walk.isPaused()) {
                duration += ceil((Date().time - walk.resumedAt!!.time) / 1000 / 60.0).toInt()
            }
            view.tvDuration.text = "$duration mins"
            if (walk.isPaused()) {
                view.btPausWalk.text = "RESUME"
                view.btPausWalk.setOnClickListener { WalkController.resumeCurrentWalk(mBaseActivity!!) }
            } else {
                view.btPausWalk.text = "PAUSE"
                view.btPausWalk.setOnClickListener { WalkController.pauseCurrentWalk(mBaseActivity!!) }
            }

            updateGoogleMapView(walk)
        }
    }

    private fun updateGoogleMapView(walk: Walk) {
        val map = mMap ?: return

        this.mWalkRoute?.remove()
        this.mWalkRoute = MapUtils.drawRouteLine(map, walk.encodedRoute, Color.BLUE)

        mStartMarker = MapUtils.createOrUpdateMarker(
            mStartMarker, map, walk.startPointLat, walk.startPointLong, "Start"
        )

        if (walk.lastPoint != null) {
            mLocationMarker = MapUtils.createOrUpdateMarker(
                mLocationMarker, map, walk.lastPoint!!.latitude, walk.lastPoint!!.longitude, "Current"
            )
            MapUtils.updateMapCamera(map, walk.lastPoint!!.latitude, walk.lastPoint!!.longitude)

        } else if (walk.resumedLat != null) {
            MapUtils.updateMapCamera(this.mMap, walk.resumedLat!!, walk.resumedLong!!)

        } else {
            MapUtils.updateMapCamera(this.mMap, walk.startPointLat, walk.startPointLong)
        }
    }

    private fun setListeners(view: View) {
        view.btEndWalk.setOnClickListener {
            mBaseActivity ?: return@setOnClickListener
            WalkController.endCurrentWalk(mBaseActivity!!)
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
