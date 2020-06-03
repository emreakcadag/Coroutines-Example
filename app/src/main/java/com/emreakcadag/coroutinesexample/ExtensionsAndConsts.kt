package com.emreakcadag.coroutinesexample

import android.app.Activity
import android.util.Log
import android.widget.Toast

const val RESULT_1 = "Result #1"
const val RESULT_2 = "Result #2"

/**
 * Get tag for all class
 */
inline val <reified T> T.TAG: String
    get() = T::class.java.simpleName

fun Activity.showToast(text: String?) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

inline fun <reified T> T.logMessage(message: String) {
    Log.d(this.TAG, message)
}