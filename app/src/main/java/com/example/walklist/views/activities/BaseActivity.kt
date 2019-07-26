package com.example.walklist.views.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.walklist.controllers.MyLocationController
import com.example.walklist.controllers.SharedPrefController
import com.example.walklist.utils.Const
import com.google.android.gms.location.LocationSettingsStates

open class BaseActivity(val mLoginRequired: Boolean) : AppCompatActivity() {

    var isShowing: Boolean = false

    override fun onStart() {
        super.onStart()
        isShowing = true
    }

    override fun onStop() {
        super.onStop()
        isShowing = false
    }


    override fun onResume() {
        super.onResume()

        if (mLoginRequired) {

            val token = SharedPrefController.get(Const.SharedPref.TOKEN, "", this)
            if (token.equals("")) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }



    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {

        when (requestCode) {
            Const.PERMISSION_REQUEST_CODE_GPS -> {

                if ((grantResults.isNotEmpty() && grantResults[0]
                                == PackageManager.PERMISSION_GRANTED)) {
                    MyLocationController.startLocationChangeNotification(this)
                } else {
                    Toast.makeText(this, "Unable to get your location", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val states = LocationSettingsStates.fromIntent(intent)
        when (requestCode) {
            Const.GPS_SETTINGS_ENABLE_REQUEST -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        MyLocationController.startLocationChangeNotification(this)
                    }

                    Activity.RESULT_CANCELED -> {
                        // Do nothing
                    }
                }
            }
        }
    }
}
