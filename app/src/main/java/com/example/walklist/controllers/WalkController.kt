package com.example.walklist.controllers

import android.app.ProgressDialog
import android.content.Context
import android.widget.Toast
import com.example.walklist.api.ApiService
import com.example.walklist.api.BaseApiCallback
import com.example.walklist.api.WalkRespModel
import com.example.walklist.api.WalksRespModel
import com.example.walklist.utils.MapUtils
import com.example.walklist.utils.PolyUtils
import com.example.walklist.utils.Walk
import com.example.walklist.views.activities.BaseActivity
import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlin.math.ceil

object WalkController : BaseController(), BaseController.Listener {

    const val DATA_TYPE_WALK_LIST = 1
    const val DATA_TYPE_ACTIVE_WALK = 2

    var mActiveWalk: Walk? = null
    var mWalks = listOf<Walk>()

    fun isWalking(): Boolean {
        return mActiveWalk != null
    }

    fun isNotWalking(): Boolean {
        return mActiveWalk == null
    }

    fun isReadyForWalking(): Boolean {
        return mActiveWalk == null
    }

    fun refreshWalksFromRemote(context: Context) {
        val pDialog = ProgressDialog.show(context, "Loading...", "Fetching walk history")
        ApiService.getService(context).getWalks().enqueue(object : BaseApiCallback<WalksRespModel>(context) {

            override fun onSuccess(result: WalksRespModel) {
                pDialog.dismiss()
                mWalks = result.data
                notifyAllListeners(DATA_TYPE_WALK_LIST)
            }

            override fun onError(message: String) {
                pDialog.dismiss()
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun refreshActiveWalkFromRemote(context: Context) {
        val pDialog = ProgressDialog.show(context, "Loading...", "Fetching walk history")
        ApiService.getService(context).getActiveWalk().enqueue(object : BaseApiCallback<WalkRespModel>(context) {

            override fun onSuccess(result: WalkRespModel) {
                pDialog.dismiss()
                mActiveWalk = result.data
                notifyAllListeners(DATA_TYPE_ACTIVE_WALK)
            }

            override fun onError(message: String) {
                pDialog.dismiss()
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun createWalk(title: String, activity: BaseActivity, success: () -> Unit) {
        val pDialogOut = ProgressDialog.show(activity, "Waiting", "Waiting for location")
        MyLocationController.startLocationChangeNotification(activity) { location ->

            pDialogOut.dismiss()
            val walk = Walk(title, location.latitude, location.longitude)

            val pDialog = ProgressDialog.show(activity, "Loading...", "Creating new walk")
            ApiService.getService(activity).createWalk(walk)
                .enqueue(object : BaseApiCallback<WalkRespModel>(activity) {

                    override fun onSuccess(result: WalkRespModel) {
                        pDialog.dismiss()
                        mActiveWalk = result.data
                        notifyAllListeners(DATA_TYPE_ACTIVE_WALK)
                        MyLocationController.startLocationChangeNotification(activity) {}
                        MyLocationController.addListener(this@WalkController)
                        success.invoke()
                    }

                    override fun onError(message: String) {
                        pDialog.dismiss()
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }

                })
        }
    }

    fun endCurrentWalk(activity: BaseActivity) {
        val activeWalk = mActiveWalk ?: return

        val pDialogOut = ProgressDialog.show(activity, "Waiting", "Waiting for location")
        MyLocationController.startLocationChangeNotification(activity) { location ->

            pDialogOut.dismiss()
            activeWalk.endPointLat = location.latitude
            activeWalk.endPointLong = location.longitude
            activeWalk.endAt = Date()

            if (!activeWalk.isPaused()) {
                activeWalk.duration += ceil((Date().time - activeWalk.resumedAt!!.time) / 1000 / 60.0).toInt()
                activeWalk.distance += MapUtils.distanceBetween(
                    activeWalk.resumedLat!!,
                    activeWalk.resumedLong!!,
                    activeWalk.endPointLat!!,
                    activeWalk.endPointLong!!
                ).toInt()
                activeWalk.encodedRoute = PolyUtils.append(
                    activeWalk.encodedRoute,
                    listOf(LatLng(activeWalk.endPointLat!!, activeWalk.endPointLong!!))
                )
            }

            MyLocationController.addListener(this@WalkController)
            MyLocationController.stopLocationChangeNotification(activity)

            val pDialog = ProgressDialog.show(activity, "Loading...", "Ending your walk")
            ApiService.getService(activity).endWalk(activeWalk.id!!, activeWalk)
                .enqueue(object : BaseApiCallback<WalkRespModel>(activity) {

                    override fun onSuccess(result: WalkRespModel) {
                        pDialog.dismiss()
                        mActiveWalk = null
                        notifyAllListeners(DATA_TYPE_ACTIVE_WALK)
                        refreshWalksFromRemote(activity)
                        refreshActiveWalkFromRemote(activity)
                    }

                    override fun onError(message: String) {
                        pDialog.dismiss()
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }

                })
        }
    }

    fun pauseCurrentWalk(activity: BaseActivity) {
        val activeWalk = mActiveWalk ?: return

        val pDialogOut = ProgressDialog.show(activity, "Waiting", "Waiting for location")
        MyLocationController.startLocationChangeNotification(activity) { location ->

            pDialogOut.dismiss()
            activeWalk.duration += ceil((Date().time - activeWalk.resumedAt!!.time) / 1000 / 60.0).toInt()
            activeWalk.distance += MapUtils.distanceBetween(
                activeWalk.resumedLat!!,
                activeWalk.resumedLong!!,
                location.latitude,
                location.longitude
            ).toInt()
            activeWalk.encodedRoute = PolyUtils.append(
                activeWalk.encodedRoute,
                listOf(LatLng(location.latitude, location.longitude))
            )
            activeWalk.resumedAt = null
            activeWalk.resumedLat = null
            activeWalk.resumedLong = null

            MyLocationController.removeListener(this)
            MyLocationController.stopLocationChangeNotification(activity)
            notifyAllListeners(DATA_TYPE_ACTIVE_WALK)

            val pDialog = ProgressDialog.show(activity, "Loading...", "Updating walk details in remote")
            ApiService.getService(activity).updateWalk(activeWalk.id!!, activeWalk)
                .enqueue(object : BaseApiCallback<WalkRespModel>(activity) {

                    override fun onSuccess(result: WalkRespModel) {
                        pDialog.dismiss()
                    }

                    override fun onError(message: String) {
                        pDialog.dismiss()
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }

                })
        }
    }

    fun resumeCurrentWalk(activity: BaseActivity) {
        val activeWalk = mActiveWalk ?: return

        val pDialogOut = ProgressDialog.show(activity, "Waiting", "Waiting for location")
        MyLocationController.startLocationChangeNotification(activity) { location ->

            pDialogOut.dismiss()
            activeWalk.resumedAt = Date()
            activeWalk.resumedLat = location.latitude
            activeWalk.resumedLong = location.longitude
            activeWalk.encodedRoute = PolyUtils.append(
                activeWalk.encodedRoute,
                listOf(LatLng(location.latitude, location.longitude))
            )
            MyLocationController.addListener(this)
            MyLocationController.startLocationChangeNotification(activity) {}
            notifyAllListeners(DATA_TYPE_ACTIVE_WALK)

            val pDialog = ProgressDialog.show(activity, "Loading...", "Updating walk details in remote")
            ApiService.getService(activity).updateWalk(activeWalk.id!!, activeWalk)
                .enqueue(object : BaseApiCallback<WalkRespModel>(activity) {

                    override fun onSuccess(result: WalkRespModel) {
                        pDialog.dismiss()
                    }

                    override fun onError(message: String) {
                        pDialog.dismiss()
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }

                })
        }
    }

    override fun dataChanged(sender: BaseController, type: Int) {
        if (sender is MyLocationController) {
            if (sender.isOfType(type, DATA_TYPE_DEFAULT)) {
                val activeWalk = mActiveWalk ?: return
                val location = MyLocationController.getLastLocation() ?: return

                val latLng = LatLng(location.latitude, location.longitude)
                activeWalk.lastPoint = latLng
                activeWalk.encodedRoute = PolyUtils.append(
                    activeWalk.encodedRoute,
                    listOf(latLng)
                )
                notifyAllListeners(DATA_TYPE_ACTIVE_WALK)
            }
        }
    }

    fun getWalk(walkId: Int): Walk? {
        return mWalks.firstOrNull { it.id == walkId }
    }
}