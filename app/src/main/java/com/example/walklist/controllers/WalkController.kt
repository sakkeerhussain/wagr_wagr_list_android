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
import com.google.android.gms.maps.model.LatLng
import java.util.*

object WalkController: BaseController() {

    const val DATA_TYPE_WALK_LIST = 1
    const val DATA_TYPE_ACTIVE_WALK = 2

    var mActiveWalk: Walk? = null
    var mWalks = listOf<Walk>()

    fun isWalking(): Boolean {
        return mActiveWalk != null
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

    fun createWalk(walk: Walk, context: Context) {
        val pDialog = ProgressDialog.show(context, "Loading...", "Creating new walk")
        ApiService.getService(context).createWalk(walk)
            .enqueue(object : BaseApiCallback<WalkRespModel>(context) {

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

    fun endCurrentWalk(context: Context) {
        val activeWalk = mActiveWalk ?: return
        // TODO - Update to the actual current point
        val currentPointLat = 11.1122
        val currentPointLong = 71.1122

        activeWalk.endPointLat = currentPointLat
        activeWalk.endPointLong = currentPointLong
        activeWalk.endAt = Date()
        activeWalk.duration += (activeWalk.resumedAt.time - Date().time) / 1000 % 60
        activeWalk.distance += MapUtils.distanceBetween(activeWalk.resumedLat, activeWalk.resumedLong, activeWalk.endPointLat!!, activeWalk.endPointLong!!).toInt()
        activeWalk.encodedRoute = PolyUtils.append(activeWalk.encodedRoute, listOf(LatLng(activeWalk.endPointLat!!, activeWalk.endPointLong!!)))

        val pDialog = ProgressDialog.show(context, "Loading...", "Ending your walk")
        ApiService.getService(context).endWalk(activeWalk.id!!, activeWalk)
            .enqueue(object : BaseApiCallback<WalkRespModel>(context) {

                override fun onSuccess(result: WalkRespModel) {
                    pDialog.dismiss()
                    mActiveWalk = null
                    notifyAllListeners(DATA_TYPE_ACTIVE_WALK)
                    refreshWalksFromRemote(context)
                    refreshActiveWalkFromRemote(context)
                }

                override fun onError(message: String) {
                    pDialog.dismiss()
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }

            })
    }
}