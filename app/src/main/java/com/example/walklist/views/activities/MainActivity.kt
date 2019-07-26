package com.example.walklist.views.activities

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.walklist.R
import com.example.walklist.utils.Walk
import com.example.walklist.views.fragments.WalkListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(true), WalkListFragment.ListInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onWalkClicked(item: Walk) {

    }
}
