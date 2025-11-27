package br.com.sailboat.uicomponent.impl.helper

import android.app.NotificationManager
import android.content.Context

class NotificationHelper {
    companion object {
        const val TASK_NOTIFICATION_ID = 1
        const val TASK_NOTIFICATION_SUMMARY_ID = 0
        const val TASK_NOTIFICATION_GROUP_KEY = "br.com.sailboat.todozy.TASK_NOTIFICATION_GROUP"

        fun notificationIdFor(taskId: Long): Int = taskId.hashCode()
    }

    fun closeNotifications(context: Context) {
        val mNotificationManager =
            context.getSystemService(
                Context
                    .NOTIFICATION_SERVICE,
            ) as NotificationManager
        mNotificationManager.cancelAll()
    }

    fun cancelTaskNotification(
        context: Context,
        taskId: Long,
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationIdFor(taskId))
    }

    fun cancelSummary(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(TASK_NOTIFICATION_SUMMARY_ID)
    }
}
