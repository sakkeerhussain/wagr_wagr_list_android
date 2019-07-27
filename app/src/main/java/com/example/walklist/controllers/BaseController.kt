package com.example.walklist.controllers

open class BaseController {

    protected val listeners = mutableListOf<Listener>()

    fun addListener(listener: Listener) {
        if (listeners.firstOrNull { it == listener } != null) { return }
        listeners.add(listener)
        listener.dataChanged(this)
    }

    fun removeListener(cartListener: Listener) {
        listeners.filter { it == cartListener }.forEach { listeners.remove(it) }
    }

    fun notifyAllListeners() {
        listeners.forEach { Runnable{ it.dataChanged(this) }.run() }
    }

    interface Listener {
        fun dataChanged(sender: BaseController)
    }
}
