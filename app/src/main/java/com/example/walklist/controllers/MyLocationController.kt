package com.example.walklist.controllers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.walklist.utils.Const
import com.example.walklist.views.activities.BaseActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*


object MyLocationController : BaseController() {

    private var onFirstLocationResp: ((Location) -> Unit)? = null
    private val mLocationRequest = createLocationRequest()
    private var mLastReceivedLocation: Location? = null
    private val mLocListener = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            if (locationResult.locations.size <= 0) return
            val location = locationResult.locations[0]
            if (location.hasAccuracy() && location.accuracy < 200) {
                mLastReceivedLocation = location
                notifyAllListeners(DATA_TYPE_DEFAULT)

                onFirstLocationResp?.invoke(location)
                onFirstLocationResp = null
            }
        }
    }

    fun startLocationChangeNotification(activity: BaseActivity, onFirstLocationResp: (Location) -> Unit): Boolean {

        this.onFirstLocationResp = onFirstLocationResp

        if (!this.hasGPSPermission(activity)) {
            requestGpsPermission(activity)
            return false
        }

        return this.requestLocIfProviderReady(activity)
    }

    fun stopLocationChangeNotification(context: Context) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.removeLocationUpdates(mLocListener)
    }

    @SuppressLint("MissingPermission")
    private fun requestLocIfProviderReady(activity: BaseActivity): Boolean {
        if (mLocationRequest == null) return false

        val builder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)

        val result = LocationServices.getSettingsClient(activity)
            .checkLocationSettings(builder.build())

        result.addOnCompleteListener(activity) { response ->
            try {
                response.getResult(ApiException::class.java)
                val fusedLocClient = LocationServices.getFusedLocationProviderClient(activity)
                fusedLocClient.requestLocationUpdates(createLocationRequest(), mLocListener, null)
            } catch (e: ApiException) {
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
                            val resolvable = e as ResolvableApiException
                            resolvable.startResolutionForResult(activity, Const.GPS_SETTINGS_ENABLE_REQUEST)
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        } catch (e: Exception) {
                            // Ignore the error.
                        }
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        Toast.makeText(activity, "Location unavailable", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return true
    }

    private fun createLocationRequest(): LocationRequest? {
        return LocationRequest.create()?.apply {
            interval = 1000
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    fun getLastLocation(): Location? {
        return mLastReceivedLocation
    }

    private fun hasGPSPermission(activity: BaseActivity): Boolean {
        val permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        return permission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestGpsPermission(activity: BaseActivity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            Const.PERMISSION_REQUEST_CODE_GPS
        )
    }
}