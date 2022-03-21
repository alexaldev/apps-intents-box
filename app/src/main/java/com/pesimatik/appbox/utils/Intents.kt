package com.pesimatik.appbox.utils

import android.content.Intent
import android.content.pm.ActivityInfo

fun newTaskActivity(activityInfo: ActivityInfo): Intent {
    return Intent(Intent.ACTION_MAIN).apply {
        setClassName(activityInfo.applicationInfo.packageName, activityInfo.name)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}