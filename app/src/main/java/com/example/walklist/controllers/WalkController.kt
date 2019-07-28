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

object WalkController : BaseController() {

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
                        MyLocationController.startLocationChangeNotification(activity) {

                        }
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
            activeWalk.duration += ((activeWalk.resumedAt.time - Date().time) / 1000 % 60).toInt()
            activeWalk.distance += MapUtils.distanceBetween(
                activeWalk.resumedLat,
                activeWalk.resumedLong,
                activeWalk.endPointLat!!,
                activeWalk.endPointLong!!
            ).toInt()
            activeWalk.encodedRoute = PolyUtils.append(
                activeWalk.encodedRoute,
                listOf(LatLng(activeWalk.endPointLat!!, activeWalk.endPointLong!!))
            )

            val pDialog = ProgressDialog.show(activity, "Loading...", "Ending your walk")
            ApiService.getService(activity).endWalk(activeWalk.id!!, activeWalk)
                .enqueue(object : BaseApiCallback<WalkRespModel>(activity) {

                    override fun onSuccess(result: WalkRespModel) {
                        pDialog.dismiss()
                        mActiveWalk = null
                        notifyAllListeners(DATA_TYPE_ACTIVE_WALK)
                        refreshWalksFromRemote(activity)
                        refreshActiveWalkFromRemote(activity)
                        MyLocationController.stopLocationChangeNotification(activity)
                    }

                    override fun onError(message: String) {
                        pDialog.dismiss()
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }

                })
        }
    }
}