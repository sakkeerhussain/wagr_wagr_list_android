package com.example.walklist.views.activities

import android.os.Bundle
import com.example.walklist.R
import kotlinx.android.synthetic.main.activity_walk.*


class WalkActivity : BaseActivity(true) {

    companion object{
        const val WALK_ID = "WALK_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walk)
        this.setSupportActionBar(toolbar)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true);
        this.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_close);

//        val walk = getWalk
        toolbar.title = "Evening walk"
    }

    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return false
    }
}
