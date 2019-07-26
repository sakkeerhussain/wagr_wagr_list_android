package com.example.walklist.utils

/**
 * A walk object representing a walk created by users.
 */
data class Walk(val title: String, val content: String, val details: String) {
    override fun toString(): String = content
}