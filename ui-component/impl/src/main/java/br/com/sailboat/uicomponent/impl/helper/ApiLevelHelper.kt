package br.com.sailboat.uicomponent.impl.helper

import android.os.Build

class ApiLevelHelper {
    fun isAtLeast(apiLevel: Int): Boolean {
        return Build.VERSION.SDK_INT >= apiLevel
    }

    fun isLowerThan(apiLevel: Int): Boolean {
        return Build.VERSION.SDK_INT < apiLevel
    }
}
