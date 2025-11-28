package br.com.sailboat.todozy.utility.android.click

import android.os.SystemClock

object SafeClick {
    const val DEFAULT_INTERVAL_MS = 600L

    private var lastClickTimestamp: Long? = null

    fun canClick(intervalMs: Long = DEFAULT_INTERVAL_MS): Boolean {
        val now = SystemClock.elapsedRealtime()
        val lastClick = lastClickTimestamp

        val canClick = lastClick == null || now - lastClick >= intervalMs
        if (canClick) {
            lastClickTimestamp = now
        }
        return canClick
    }
}
