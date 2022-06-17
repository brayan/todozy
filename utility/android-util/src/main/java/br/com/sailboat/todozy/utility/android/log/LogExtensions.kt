package br.com.sailboat.todozy.utility.android.log

import android.util.Log

fun Throwable.log() = Log.e("DEFAULT_ERROR_LOG", "Default error", this)

fun String.logDebug() = Log.d("DEFAULT_DEBUG_LOG", this)
