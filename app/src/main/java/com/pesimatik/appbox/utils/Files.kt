package com.pesimatik.appbox.utils

import android.content.Context
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 * Writes the provided [obj] to a file with the specified [filename].
 * @param context Necessary for getting an [ObjectOutputStream]
 * @param obj The [Serializable] object to be stored
 * @throws IOException
 */
fun <T : Serializable> writeObjToFile(context: Context, obj: T, filename: String) {
    context.openFileOutput(filename, Context.MODE_PRIVATE).use {
        ObjectOutputStream(it).use { ooStream ->
            ooStream.writeObject(obj)
        }
    }
}

fun <T : Serializable> readObjFromFile(context: Context, filename: String): T {
    return context.openFileInput(filename).use {
        ObjectInputStream(it).use { ooStream ->
            ooStream.readObject() as T
        }
    }
}