package com.pesimatik.appbox.ui

import com.pesimatik.appbox.INITIAL_FONT_SIZE

object UsageToSizeDispatcher {
    fun calculateFontSizes(usages: Map<String, Float>): Map<String, Float> {
        val max = globalMin()
        val usagesSum = usages.values.sum()
        val scaler = SizesScaler.GeometricScaler(max, usagesSum)
        return usages.mapValues { scaler.scale(it) }
    }

    fun globalMin(): Float {
        return 14f // Could fetch dynamically from the dpi of the device
    }

    fun calculateFontSizes(activity: String, usages: Map<String, Float>): Float {
        return calculateFontSizes(usages)[activity] ?: INITIAL_FONT_SIZE
    }

    sealed class SizesScaler {
        abstract fun scale(e: Map.Entry<String, Float>): Float

        class GeometricScaler(
            val maxSize: Float,
            val mapValuesSum: Float
        ) : SizesScaler() {
            override fun scale(e: Map.Entry<String, Float>): Float {
                return maxSize * (mapValuesSum / (mapValuesSum - e.value))
            }
        }
    }
}


