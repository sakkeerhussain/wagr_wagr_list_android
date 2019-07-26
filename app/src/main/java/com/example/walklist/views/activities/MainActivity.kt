package com.example.walklist.views.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.walklist.R
import com.example.walklist.controllers.UserController
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.ic_logout -> {
                UserController.logout(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onWalkClicked(item: Walk) {

    }
}
