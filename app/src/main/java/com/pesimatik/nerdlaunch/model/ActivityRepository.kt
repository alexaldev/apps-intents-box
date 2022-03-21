package com.pesimatik.nerdlaunch.model

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo

object ActivityRepository {
    fun fetchActivities(
        packageManager: PackageManager,
        sorter: ((ResolveInfo, ResolveInfo) -> Int)
    ): List<ActivityEntry> {
        val queryIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        with(packageManager.queryIntentActivities(queryIntent, 0)) {
            sortWith(sorter)
            return this.map {
                ActivityEntry(
                    it.loadLabel(packageManager).toString(),
                    it
                )
            }
        }
    }
}