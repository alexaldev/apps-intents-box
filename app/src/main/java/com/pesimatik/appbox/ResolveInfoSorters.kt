package com.pesimatik.appbox

import android.content.pm.PackageManager
import android.content.pm.ResolveInfo

fun alphabeticSorter(pm: PackageManager): (ResolveInfo, ResolveInfo) -> Int {
    return { a: ResolveInfo, b: ResolveInfo ->
        String.CASE_INSENSITIVE_ORDER.compare(
            a.loadLabel(pm).toString(),
            b.loadLabel(pm).toString()
        )
    }
}

fun alphabeticSorter(): (String, String) -> Int {
    return {s1, s2 -> String.CASE_INSENSITIVE_ORDER.compare(s1, s2) }
}

fun installationTimeSorter(pm: PackageManager): (ResolveInfo, ResolveInfo) -> Int {
    return { a: ResolveInfo, b: ResolveInfo ->
        (firstInstallTimeOf(a, pm) - firstInstallTimeOf(b, pm)).toInt()
    }
}

private fun firstInstallTimeOf(ri: ResolveInfo, packageManager: PackageManager): Long {
    return packageManager.getPackageInfo(ri.activityInfo.packageName, 0).firstInstallTime
}
