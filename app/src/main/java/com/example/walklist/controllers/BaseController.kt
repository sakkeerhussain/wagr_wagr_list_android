package com.example.walklist.controllers

open class BaseController {

    companion object {
        const val DATA_TYPE_DEFAULT = 0
    }
    protected val listeners = mutableListOf<Listener>()

    fun addListener(listener: Listener) {
        if (listeners.firstOrNull { it == listener } != null) { return }
        listeners.add(listener)
        listener.dataChanged(this, DATA_TYPE_DEFAULT)
    }

    fun removeListener(listener: Listener) {
        listeners.filter { it == listener }.forEach { listeners.remove(it) }
    }

    fun notifyAllListeners(type: Int) {
        listeners.forEach { Runnable{ it.dataChanged(this, type) }.run() }
    }

    fun isOfType(typeRec: Int, typeValid: Int): Boolean {
        return typeRec == DATA_TYPE_DEFAULT || typeRec == typeValid
    }

    interface Listener {
        fun dataChanged(sender: BaseController, type: Int)
    }
}
