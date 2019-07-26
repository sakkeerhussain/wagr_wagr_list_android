package com.example.walklist.controllers

import android.app.ProgressDialog
import android.content.Context
import android.widget.Toast
import com.example.walklist.api.ApiService
import com.example.walklist.api.BaseApiCallback
import com.example.walklist.api.WalksRespModel
import com.example.walklist.utils.Walk
import com.example.walklist.views.activities.BaseActivity

object WalkController {

    // TODO - Create a pub-sub model to connect views to this controllers

    private val mActiveWalk: Walk? = null

    fun isWalking(): Boolean {
        return mActiveWalk != null
    }

    fun getWalks(context: Context, listener: WalkListener) {
        val pDialog = ProgressDialog.show(context, "Loading...", "Fetching walk history")
        ApiService.getService(context).getWalks().enqueue(object : BaseApiCallback<WalksRespModel>(context) {

                override fun onSuccess(result: WalksRespModel) {
                    pDialog.dismiss()
                    listener.list(result.data)
                }

                override fun onError(message: String) {
                    pDialog.dismiss()
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    interface WalkListener {
        fun list(walks: List<Walk>)
    }
}