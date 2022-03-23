package com.pesimatik.appbox.model

import android.content.Context
import android.util.Log
import com.pesimatik.appbox.utils.readObjFromFile
import com.pesimatik.appbox.utils.writeObjToFile
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*
import kotlin.collections.HashMap

private const val USAGES_FILE = "usages"
private const val TAG = "UsagesRepository"

object ActivityUsagesRepository {

    fun entriesHistoryExists(context: Context): Boolean {
        return getUsages(context).isNotEmpty()
    }

    fun getUsages(context: Context): HashMap<String, Float> {
        return try {
            val res = readObjFromFile(context, USAGES_FILE) as HashMap<String, Float>
            Log.d(TAG, "Found usages file containing : ${res.size} entries.")
            res
        } catch (e: IOException) {
            hashMapOf()
        }
    }

    fun storeUsages(context: Context, usages: Map<String, Float>) {
        writeObjToFile(context, HashMap(usages), USAGES_FILE)
    }

    // Some Kotlin fun - [pun intended, I am so funny]
    private infix fun Properties.createFrom(map: Map<*, *>): Properties {
        map.forEach { this[it.key] = it.value }
        return this
    }
}