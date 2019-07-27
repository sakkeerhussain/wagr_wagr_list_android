package com.example.walklist.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.walklist.R
import com.example.walklist.controllers.BaseController
import com.example.walklist.controllers.UserController
import com.example.walklist.controllers.WalkController
import com.example.walklist.utils.Walk
import com.example.walklist.views.fragments.WalkListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(true), BaseController.Listener, WalkListFragment.ListInteractionListener {

    override var listFragment: WalkListFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setupViews()
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        WalkController.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        WalkController.removeListener(this)
    }


    private fun setupViews() {
        setupNewWalkButton()
    }

    private fun setupNewWalkButton() {
        if (WalkController.isReadyForWalking()) {
            fab.show()
        } else {
            fab.hide()
        }
    }

    private fun setListeners() {

        fab.setOnClickListener { view ->
            val walk = Walk("Test walk", 11.1111, 71.1111)
            WalkController.createWalk(walk, this)
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
            R.id.ic_refresh -> {
                WalkController.refreshWalksFromRemote(this)
                WalkController.refreshActiveWalkFromRemote(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onWalkClicked(walk: Walk) {
        val intent = Intent(this, WalkActivity::class.java)
        intent.putExtra(WalkActivity.WALK_ID, walk.id)
        startActivity(intent)
    }

    override fun dataChanged(sender: BaseController, type: Int) {
        runOnUiThread {
            if (sender is WalkController) {
                if (sender.isOfType(type, WalkController.DATA_TYPE_WALK_LIST)) {
                    listFragment?.refreshWalks()
                } else {
                    setupNewWalkButton()
                }
            }
        }
    }

}
