package com.example.walklist.utils

import android.location.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

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


    fun updateMapCamera(map: GoogleMap?, latitude: Double, longitude: Double) {
        val latLong = LatLng(latitude, longitude)
        val cameraPosition = CameraPosition.Builder()
            .target(latLong)
            .zoom(12f)
            .bearing(0f)
            .tilt(0f)   // Sets the tilt of the camera to 30 degrees
            .build()

        map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    fun drawRouteLine(map: GoogleMap?, line: String?, color: Int): Polyline? {
        line ?: return null
        val points = PolyUtils.decode(line)
        val polyLineOptions = PolylineOptions()
        for (latLong in points) {
            polyLineOptions.add(latLong)
        }
        polyLineOptions.width(15f)
        polyLineOptions.color(color)
        polyLineOptions.geodesic(true)
        return map?.addPolyline(polyLineOptions)
    }

    fun createMarker(map: GoogleMap?, lat: Double, long: Double, title: String): Marker? {
        return map?.addMarker(
            MarkerOptions()
                .position(LatLng(lat, long))
                .flat(true)
                .rotation(0f)
                .title(title)
        )
    }
}