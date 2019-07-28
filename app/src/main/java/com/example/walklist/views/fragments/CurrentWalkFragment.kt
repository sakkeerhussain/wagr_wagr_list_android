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
import com.example.walklist.utils.MapUtils
import kotlinx.android.synthetic.main.fragment_current_walk.*
import kotlinx.android.synthetic.main.fragment_current_walk.view.*
import java.util.*

class CurrentWalkFragment : Fragment(), BaseController.Listener {

    var mView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView =  inflater.inflate(R.layout.fragment_current_walk, container, false)
        setupViews()
        setListeners(mView!!)
        WalkController.refreshActiveWalkFromRemote(mView!!.context)
        return mView
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
        val view = mView ?: return
        val walk = WalkController.mActiveWalk

        if (walk == null) {
            view.flRoot.visibility = View.GONE
        } else {
            view.flRoot.visibility = View.VISIBLE
            view.tvTitle.text = walk.title
            view.tvDistance.text = "${walk.distanceKM()} KM"
            view.tvDuration.text = "${walk.duration} mins"
        }
    }

    private fun setListeners(view: View) {
        view.btEndWalk.setOnClickListener {
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
