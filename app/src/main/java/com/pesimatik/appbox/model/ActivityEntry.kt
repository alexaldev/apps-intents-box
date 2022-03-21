package com.pesimatik.appbox.model

import android.content.pm.ResolveInfo

data class ActivityEntry(
    val name: String,
    val resolveInfo: ResolveInfo
)