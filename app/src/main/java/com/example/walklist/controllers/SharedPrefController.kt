package com.example.walklist.controllers

import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.GsonBuilder

object SharedPrefController {

    fun store(name: String, value: Any, context: Context) {
        val valueStr = GsonBuilder().create().toJson(value)
        val pref = PreferenceManager.getDefaultSharedPreferences(context).edit()
        pref.putString(name, valueStr)
        pref.apply()
    }

    fun get(name: String, default: Any, context: Context): Any {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)

        if (!pref.contains(name)) return default

        val valueStr = pref.getString(name, "{}")
        return GsonBuilder().create().fromJson<Any>(valueStr, default::class.java)
    }
}