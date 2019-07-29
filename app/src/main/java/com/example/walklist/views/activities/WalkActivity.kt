package com.example.walklist.views.activities

import android.graphics.Color
import android.os.Bundle
import com.example.walklist.R
import com.example.walklist.controllers.WalkController
import com.example.walklist.utils.MapUtils
import com.example.walklist.utils.Walk
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polyline
import kotlinx.android.synthetic.main.activity_walk.*


class WalkActivity : BaseActivity(true) {

    private var mStartMarker: Marker? = null
    private var mEndMarker: Marker? = null
    private var mWalkRoute: Polyline? = null
    private var mMap: GoogleMap? = null
    private var mAutoUpdateMapCamera = true

    companion object{
        const val WALK_ID = "WALK_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walk)
        this.setSupportActionBar(toolbar)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_close)

        val walkId = intent.getIntExtra(WALK_ID, -1)
        if (walkId == -1) return

        val walk = WalkController.getWalk(walkId) ?: return

        setupViews(walk)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            this.mMap = it
            updateMapCamera(walk.startPointLat, walk.startPointLong)
            setupMapActionListeners()
            mWalkRoute = drawRouteLine(walk.encodedRoute, Color.BLUE)
            mStartMarker = createMarker(walk.startPointLat, walk.startPointLong, "Start")
            mEndMarker = createMarker(walk.endPointLat!!, walk.endPointLong!!, "End")
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun setupViews(walk: Walk) {
        toolbar.title = walk.title
        tvDistance.text = walk.distanceStr()
        tvDuration.text = walk.durationStr()
    }

    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return false
    }

    private fun setupMapActionListeners() {
        mMap?.setOnCameraMoveStartedListener {
            if (it == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                mAutoUpdateMapCamera = false
            }
        }

        btLocation.setOnClickListener {
            mAutoUpdateMapCamera = true
            btLocation.hide()
        }
    }

    private fun updateMapCamera(latitude: Double, longitude: Double) {
        if (mAutoUpdateMapCamera) {
            MapUtils.updateMapCamera(mMap, latitude, longitude)
        }
    }

    private fun drawRouteLine(line: String?, color: Int): Polyline? {
        return MapUtils.drawRouteLine(mMap, line, color)
    }

    private fun createMarker(lat: Double, long: Double, title: String): Marker? {
        return MapUtils.createMarker(mMap, lat, long, title)
    }
}
