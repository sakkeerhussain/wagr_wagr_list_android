package com.example.walklist.utils

import com.google.gson.annotations.SerializedName

/**
 * A walk object representing a walk created by users.
 */
data class Walk(val id: Int?, val title: String, val distance: Double?, val duration: Double?,
                @SerializedName("start_point_lat") val startPointLat: Double,
                @SerializedName("start_point_long") val startPointLong: Double,
                @SerializedName("end_point_lat") val endPointLat: Double?,
                @SerializedName("end_point_long") val endPointLong: Double?) {

    constructor(title: String, startPointLat: Double, startPointLong: Double) :
            this(null, title, null, null, startPointLat, startPointLong, null, null)

    override fun toString(): String = title
    fun description(): String = "${distance ?: 0/1000} KM, ${duration ?: 0} mins"
}


class User(val email: String, val password: String, val name: String?)