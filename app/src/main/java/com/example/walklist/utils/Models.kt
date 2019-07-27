package com.example.walklist.utils

import com.google.gson.annotations.SerializedName

/**
 * A walk object representing a walk created by users.
 */
data class Walk(val id: Int, val title: String) {
    override fun toString(): String = title
}


class User(val email: String, val password: String, val name: String?)