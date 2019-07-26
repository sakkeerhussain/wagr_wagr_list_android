package com.example.walklist.dummy

import com.example.walklist.utils.Walk
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object DummyContent {

    /**w
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<Walk> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, Walk> = HashMap()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createDummyItem(i))
        }
    }

    private fun addItem(item: Walk) {
        ITEMS.add(item)
        ITEM_MAP.put(item.title, item)
    }

    private fun createDummyItem(position: Int): Walk {
        return Walk(
            position,
            "Evening Walk - $position",
            "$position KM : 54 mins",
            makeDetails(position)
        )
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0 until position) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }
}
