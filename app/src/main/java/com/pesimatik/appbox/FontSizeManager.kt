package com.pesimatik.appbox

import android.content.SharedPreferences
import java.lang.IllegalArgumentException

object FontSizeManager {
    fun getFontScale(prefs: SharedPreferences): Float {
        return when(prefs.getString(PREFS_TEXT_SIZE, FONT_SIZE_NORMAL)) {
            FONT_SIZE_SMALL -> 0.7f
            FONT_SIZE_NORMAL -> 1.0f
            FONT_SIZE_BIG -> 1.5f
            else -> throw IllegalArgumentException("Cannot dispatch stored text size.")
        }
    }
}