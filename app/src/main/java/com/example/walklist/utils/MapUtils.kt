package com.example.walklist.utils

import android.location.Location

object MapUtils {

    fun distanceBetween(startLatitude: Double, startLongitude: Double, endLatitude: Double, endLongitude: Double): Float {
        val loc1 = Location("loc1")
        loc1.latitude = startLatitude
        loc1.longitude = startLongitude

        val loc2 = Location("loc2")
        loc2.latitude = endLatitude
        loc2.longitude = endLongitude

        return loc1.distanceTo(loc2)
    }
}