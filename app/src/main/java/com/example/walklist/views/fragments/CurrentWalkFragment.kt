package com.example.walklist.views.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.walklist.R
import com.example.walklist.controllers.BaseController
import com.example.walklist.controllers.WalkController
import kotlinx.android.synthetic.main.fragment_current_walk.*

class CurrentWalkFragment : Fragment(), BaseController.Listener {

    var mView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView =  inflater.inflate(R.layout.fragment_current_walk, container, false)
        setupViews()
        setListeners()
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
        val walk = WalkController.getActiveWalk()

        if (walk == null) {
            root?.visibility = View.GONE
        } else {
            root?.visibility = View.VISIBLE
            tvTitle?.text = walk.title
            tvDistance?.text = walk.distance.toString()
            tvDuration?.text = walk.duration.toString()
        }
    }

    private fun setListeners() {
        btEndWalk?.setOnClickListener {
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
