package com.example.walklist.utils

import com.google.gson.annotations.SerializedName

/**
 * A walk object representing a walk created by users.
 */
data class Walk(val id: Int, val title: String, val distance: Double?, val duration: Double?) {

    override fun toString(): String = title
    fun description(): String = "${distance ?: 0/1000} KM, ${duration ?: 0} mins"
}


class User(val email: String, val password: String, val name: String?)