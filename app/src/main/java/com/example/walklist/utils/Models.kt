package com.example.walklist.utils

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * A walk object representing a walk created by users.
 */
data class Walk(val id: Int?, val title: String, var distance: Double, var duration: Double,
                @SerializedName("started_at") var startedAt: Date,
                @SerializedName("start_point_lat") val startPointLat: Double,
                @SerializedName("start_point_long") val startPointLong: Double,
                @SerializedName("end_at") var endAt: Date?,
                @SerializedName("end_point_lat") var endPointLat: Double?,
                @SerializedName("end_point_long") var endPointLong: Double?,
                @SerializedName("resumed_at") var resumedAt: Date,
                @SerializedName("resumed_lat") var resumedLat: Double?,
                @SerializedName("resumed_long") var resumedLong: Double?,
                @SerializedName("encoded_route") var encodedRoute: String) {

    constructor(title: String, startPointLat: Double, startPointLong: Double) :
            this(null, title, 0.0, 0.0, Date(), startPointLat, startPointLong, null, null, null,
                Date(), startPointLat, startPointLong, PolyUtils.encode(listOf(LatLng(startPointLat, startPointLong))))

    override fun toString(): String = title
    fun description(): String = "${distance/1000} KM, $duration mins"
    fun distanceKM(): String = String.format("%.3f", this.distance)
}


class User(val email: String, val password: String, val name: String?)