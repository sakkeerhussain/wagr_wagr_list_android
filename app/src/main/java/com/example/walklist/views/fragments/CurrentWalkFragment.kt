package com.example.walklist.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.walklist.R
import com.example.walklist.controllers.WalkController
import kotlinx.android.synthetic.main.fragment_current_walk.view.*

class CurrentWalkFragment : Fragment() {

    lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView =  inflater.inflate(R.layout.fragment_current_walk, container, false)
        updateActiveView()
        return mView
    }

    fun updateActiveView() {
        val walk = WalkController.getActiveWalk()

        if (walk == null) {
            mView.visibility = View.GONE
        } else {
            mView.visibility = View.VISIBLE
            mView.tvTitle.text = walk.title
            mView.tvDistance.text = walk.distance.toString()
            mView.tvDuration.text = walk.duration.toString()
        }
    }
}
