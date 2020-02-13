package com.github.redlann.extensions

import android.os.Looper
import android.util.Log

fun Any.isMainThread(context: String = "") {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        // Current Thread is Main Thread.
        Log.d("ThreadCheck", "ThreadCheck MainThread $context")
    } else {
        Log.d("ThreadCheck", "ThreadCheck NOT MainThread $context")
    }
}