package com.example.walklist.utils

import com.google.gson.annotations.SerializedName

/**
 * A walk object representing a walk created by users.
 */
data class Walk(val id: Int, val title: String, val content: String, val details: String) {
    override fun toString(): String = content
}



class User(val email: String, val password: String, val name: String?)