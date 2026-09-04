package com.tighlah.launcher.core.logging

import android.util.Log

object TighlahLogger {
    private const val TAG = "TighlahLauncher"

    fun d(tag: String, message: String, throwable: Throwable? = null) {
        Log.d("$TAG/$tag", message, throwable)
    }

    fun i(tag: String, message: String, throwable: Throwable? = null) {
        Log.i("$TAG/$tag", message, throwable)
    }

    fun w(tag: String, message: String, throwable: Throwable? = null) {
        Log.w("$TAG/$tag", message, throwable)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        Log.e("$TAG/$tag", message, throwable)
    }
}
