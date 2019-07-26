package com.example.walklist.utils

import com.google.gson.annotations.SerializedName

/**
 * A walk object representing a walk created by users.
 */
data class Walk(val title: String, val content: String, val details: String) {
    override fun toString(): String = content
}



class User(val email: String, val password: String,
              @SerializedName("first_name") val firstName: String?,
              @SerializedName("last_name") val lastName: String?) {

    fun getName(): String {
        return "$firstName ${lastName ?: ""}"
    }
}