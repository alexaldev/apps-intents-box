package com.pesimatik.appbox.ui

object UsageToSizeDispatcher {
    fun calculateFontSizes(usages: Map<String, Int>): Map<String, Float> {
        val globalFontSize = 15f
        val usagesSum = usages.values.sum()
        return usages.mapValues { globalFontSize + (globalFontSize * it.value.toFloat() / usagesSum) * 4 }
    }

    fun calculateFontSizes(activity: String, usages: Map<String, Int>): Float {
        return calculateFontSizes(usages)[activity] ?: 15f
    }
}


