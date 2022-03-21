package com.pesimatik.appbox.model

import android.content.Context
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object ActivityUsagesRepository {
    fun getUsages(context: Context): Map<String, Float> {
        val f = File(context.filesDir, "usages")
        return if (!f.exists()) {
            f.createNewFile()
            emptyMap()
        } else {
            f.inputStream().use {
                ObjectInputStream(it).readObject() as Map<String, Float>
            }
        }
    }

    fun storeUsages(context: Context, usages: Map<String, Float>) {
        File(context.filesDir, "usages").outputStream().use {
            ObjectOutputStream(it).writeObject(usages)
        }
    }
}