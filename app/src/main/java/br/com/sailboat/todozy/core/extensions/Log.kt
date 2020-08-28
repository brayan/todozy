package br.com.sailboat.todozy.core.extensions

import timber.log.Timber

const val LOG_TAG = "Todozy"

fun Throwable.log() = Timber.e(this)

fun String.logDebug() = Timber.tag(LOG_TAG).d(this)