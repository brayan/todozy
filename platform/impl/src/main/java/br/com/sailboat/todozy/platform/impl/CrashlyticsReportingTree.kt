package br.com.sailboat.todozy.platform.impl

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CrashlyticsReportingTree : Timber.Tree() {
    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }

        t?.apply {
            if (priority == Log.ERROR || priority == Log.WARN) {
                FirebaseCrashlytics.getInstance().recordException(t)
            }
        }
    }
}
