package br.com.sailboat.todozy.core.platform.receivers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.extensions.log
import br.com.sailboat.todozy.core.extensions.logDebug
import br.com.sailboat.todozy.core.extensions.safe
import br.com.sailboat.todozy.core.presentation.helper.NotificationHelper
import br.com.sailboat.todozy.features.LauncherActivity
import br.com.sailboat.todozy.features.settings.domain.usecase.GetAlarmSoundSetting
import br.com.sailboat.todozy.features.settings.domain.usecase.GetAlarmVibrateSetting
import br.com.sailboat.todozy.features.tasks.domain.model.TaskCategory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskFilter
import br.com.sailboat.todozy.features.tasks.domain.usecase.GetTask
import br.com.sailboat.todozy.features.tasks.domain.usecase.GetTasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AlarmReceiver : BroadcastReceiver(), KoinComponent {

    val getTask: GetTask by inject()
    val getTasks: GetTasks by inject()
    val getAlarmSoundSetting: GetAlarmSoundSetting by inject()
    val getAlarmVibrateSetting: GetAlarmVibrateSetting by inject()

    override fun onReceive(context: Context, intent: Intent) {
        "${javaClass.simpleName}.onReceive()".logDebug()
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val builder = buildNotification(context, intent)
                throwNotification(context, builder)
            } catch (e: Exception) {
                e.log()
            }
        }
    }

    private suspend fun throwNotification(context: Context, builder: NotificationCompat.Builder) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.makeNotificationChannel(context)
        }
        notificationManager.notify(NotificationHelper().TASK_NOTIFICATION_ID, builder.build())
    }

    private suspend fun buildNotification(context: Context, intent: Intent): NotificationCompat.Builder {
        val resultIntent = Intent(context, LauncherActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID_ALARM)
                .setSmallIcon(R.drawable.ic_vec_notification_icon)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .apply { priority = NotificationCompat.PRIORITY_HIGH }
                .apply { color = ContextCompat.getColor(context, R.color.md_blue_500) }

        initContentTextAndTitle(context, intent, builder)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            initVibrate(builder)
            getAlarmSoundSetting()?.run { builder.setSound(this) }
        }

        return builder
    }

    private suspend fun initContentTextAndTitle(context: Context, intent: Intent, builder: NotificationCompat.Builder) {
        try {
            val tasks = getTasks(TaskFilter(TaskCategory.BEFORE_NOW))

            if (tasks.size <= 1) {
                initContentFromIntent(intent, builder)
            } else {
                setTextAndTitleFromList(context, builder, tasks.size)
            }

        } catch (e: Exception) {
            e.log()
            builder.setContentTitle(context.getString(R.string.new_task))
            builder.setContentText(context.getString(R.string.touch_to_check))
        }
    }

    private suspend fun initContentFromIntent(intent: Intent, builder: NotificationCompat.Builder) {
        val taskId = intent.getLongExtra(EXTRA_TASK_ID, -1)

        val task = getTask(taskId)
        builder.setContentTitle(task.name)

        if (task.notes?.isNotEmpty().safe()) {
            builder.setContentText(task.notes)
        }
    }

    private fun setTextAndTitleFromList(context: Context, builder: NotificationCompat.Builder, tasksQuantity: Int) {
        builder.setContentTitle(tasksQuantity.toString() + " " + context.getString(R.string.tasks_to_do))
        builder.setContentText(context.getString(R.string.touch_to_check))
    }

    private suspend fun initVibrate(builder: NotificationCompat.Builder) {
        if (getAlarmVibrateSetting()) {
            builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE or NotificationCompat.DEFAULT_LIGHTS)
        } else {
            builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun NotificationManager.makeNotificationChannel(context: Context) {
        createNotificationChannel(
                NotificationChannel(
                        CHANNEL_ID_ALARM,
                        context.getString(R.string.notification_title_task_alarms),
                        NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                    enableLights(true)
                    enableVibration(true)
                }
        )
    }

    companion object {
        const val EXTRA_TASK_ID = "EXTRA_TASK_ID"
        const val CHANNEL_ID_ALARM = "CHANNEL_ID_ALARM"
    }

}