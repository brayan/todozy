package br.com.sailboat.todozy.core.platform

import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber

class CrashlyticsReportingTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }

        t?.apply {
            if (priority == Log.ERROR || priority == Log.WARN) {
                Crashlytics.logException(t)
            }
        }
    }

}