package com.example.walklist.controllers

import android.app.ProgressDialog
import android.content.Context
import android.widget.Toast
import com.example.walklist.api.ApiService
import com.example.walklist.api.BaseApiCallback
import com.example.walklist.api.WalkRespModel
import com.example.walklist.api.WalksRespModel
import com.example.walklist.utils.Walk

object WalkController: BaseController() {

    const val DATA_TYPE_WALK_LIST = 1
    const val DATA_TYPE_ACTIVE_WALK = 2

    private var mActiveWalk: Walk? = null
    private var mWalks = listOf<Walk>()

    fun getActiveWalk(): Walk? {
        return mActiveWalk
    }

    fun getWalks(): List<Walk> {
        return mWalks
    }

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
        val walk = mActiveWalk ?: return
        val pDialog = ProgressDialog.show(context, "Loading...", "Ending your walk")
        ApiService.getService(context).endWalk(walk.id!!, walk)
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