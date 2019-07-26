package com.example.walklist.views.activities

import androidx.appcompat.app.AppCompatActivity
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

//        val walk = getWalk
        toolbar.title = "Evening walk"
    }
}
