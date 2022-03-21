package com.pesimatik.nerdlaunch.ui

import com.pesimatik.nerdlaunch.FONT_SIZE_NORMAL
import java.util.HashMap

object UsageToSizeDispatcher {
    fun calculateFontSizes(usages: Map<String, Int>): Map<String, Float> {
        val globalFontSize = 15f
        val usagesSum = usages.values.sum()
        return usages.mapValues { globalFontSize + (globalFontSize * it.value.toFloat() / usagesSum) + (globalFontSize * it.value.toFloat() / usagesSum) * 3 }
    }

    fun calculateFontSizes(activity: String, usages: Map<String, Int>): Float {
        return calculateFontSizes(usages)[activity] ?: 15f
    }
}


