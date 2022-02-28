package br.com.sailboat.todozy.uicomponent.helper

import android.app.NotificationManager
import android.content.Context

class NotificationHelper {

    val TASK_NOTIFICATION_ID = 1

    fun closeNotifications(context: Context) {
        val mNotificationManager = context.getSystemService(
            Context
                .NOTIFICATION_SERVICE
        ) as NotificationManager
        mNotificationManager.cancel(TASK_NOTIFICATION_ID)
    }

}