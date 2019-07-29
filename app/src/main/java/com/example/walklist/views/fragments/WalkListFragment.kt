package com.example.walklist.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walklist.R
import com.example.walklist.controllers.WalkController
import com.example.walklist.utils.Walk
import com.example.walklist.views.adapters.WalkListAdapter
import com.google.android.gms.maps.MapView
import kotlinx.android.synthetic.main.fragment_walk_list.view.*

class WalkListFragment : Fragment() {

    private lateinit var mWalksAdapter: WalkListAdapter
    private lateinit var mView: View
    private var listener: ListInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_walk_list, container, false)
        mWalksAdapter = WalkListAdapter(listener)
        this.mView = view

        // Set the adapter
        with(mView.rvList) {
            layoutManager = LinearLayoutManager(context)
            adapter = mWalksAdapter
        }

        WalkController.refreshWalksFromRemote(view.context)
        return view
    }

    fun refreshWalks() {
        val walks = WalkController.mWalks
        // Updating empty message
        mView.tvEmptyMsg.visibility = if (walks.isEmpty()) View.VISIBLE else View.GONE

        // Updating data
        mWalksAdapter.setData(walks)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ListInteractionListener) {
            listener = context
            context.listFragment = this
        } else {
            throw RuntimeException("$context must implement ListInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        (context as ListInteractionListener).listFragment = null
    }

    interface ListInteractionListener {
        var listFragment: WalkListFragment?
        fun onWalkClicked(walk: Walk, mapView: MapView)
    }
}
