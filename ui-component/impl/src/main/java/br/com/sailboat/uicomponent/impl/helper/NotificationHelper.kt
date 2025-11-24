package br.com.sailboat.uicomponent.impl.helper

import android.app.NotificationManager
import android.content.Context

class NotificationHelper {
    companion object {
        const val TASK_NOTIFICATION_ID = 1
    }

    fun closeNotifications(context: Context) {
        val mNotificationManager =
            context.getSystemService(
                Context
                    .NOTIFICATION_SERVICE,
            ) as NotificationManager
        mNotificationManager.cancel(TASK_NOTIFICATION_ID)
    }
}
