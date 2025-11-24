package br.com.sailboat.todozy.feature.alarm.impl.presentation.broadcastreceiver

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
import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.domain.model.TaskFilter
import br.com.sailboat.todozy.feature.navigation.android.SplashNavigator
import br.com.sailboat.todozy.feature.settings.android.domain.usecase.GetAlarmSoundSettingUseCase
import br.com.sailboat.todozy.feature.settings.domain.usecase.GetAlarmVibrateSettingUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.feature.task.list.domain.usecase.GetTasksUseCase
import br.com.sailboat.todozy.utility.android.intent.getPendingIntentFlags
import br.com.sailboat.todozy.utility.android.log.log
import br.com.sailboat.todozy.utility.android.log.logDebug
import br.com.sailboat.todozy.utility.kotlin.extension.isTrue
import br.com.sailboat.uicomponent.impl.helper.NotificationHelper
import br.com.sailboat.uicomponent.impl.R as UiR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class AlarmReceiver : BroadcastReceiver(), KoinComponent {

    private val getTaskUseCase: GetTaskUseCase by inject()
    private val getTasksUseCase: GetTasksUseCase by inject()
    private val getAlarmSoundSettingUseCase: GetAlarmSoundSettingUseCase by inject()
    private val getAlarmVibrateSettingUseCase: GetAlarmVibrateSettingUseCase by inject()
    private val splashNavigator: SplashNavigator by inject()

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

    private fun throwNotification(context: Context, builder: NotificationCompat.Builder) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.makeNotificationChannel(context)
        }
        notificationManager.notify(NotificationHelper().TASK_NOTIFICATION_ID, builder.build())
    }

    private suspend fun buildNotification(
        context: Context,
        intent: Intent
    ): NotificationCompat.Builder {
        val resultIntent = splashNavigator.getSplashIntent(context)
        val resultPendingIntent = PendingIntent.getActivity(
            context,
            0,
            resultIntent,
            getPendingIntentFlags()
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID_ALARM)
            .setSmallIcon(UiR.drawable.ic_vec_notification_icon)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)
            .apply { priority = NotificationCompat.PRIORITY_HIGH }
            .apply { color = ContextCompat.getColor(context, UiR.color.md_blue_500) }

        initContentTextAndTitle(context, intent, builder)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            initVibrate(builder)
            getAlarmSoundSettingUseCase()?.run { builder.setSound(this) }
        }

        return builder
    }

    private suspend fun initContentTextAndTitle(
        context: Context,
        intent: Intent,
        builder: NotificationCompat.Builder
    ) {
        try {
            val taskFilter = TaskFilter(category = TaskCategory.BEFORE_NOW)
            val tasks = getTasksUseCase(taskFilter).getOrDefault(emptyList())

            if (tasks.size <= 1) {
                initContentFromIntent(intent, builder)
            } else {
                setTextAndTitleFromList(context, builder, tasks.size)
            }
        } catch (e: Exception) {
            e.log()
            builder.setContentTitle(context.getString(UiR.string.new_task))
            builder.setContentText(context.getString(UiR.string.touch_to_check))
        }
    }

    private suspend fun initContentFromIntent(intent: Intent, builder: NotificationCompat.Builder) {
        val taskId = intent.getLongExtra(EXTRA_TASK_ID, -1)

        val task = getTaskUseCase(taskId).getOrThrow()
        builder.setContentTitle(task.name)

        if (task.notes?.isNotEmpty().isTrue()) {
            builder.setContentText(task.notes)
        }
    }

    private fun setTextAndTitleFromList(
        context: Context,
        builder: NotificationCompat.Builder,
        tasksQuantity: Int
    ) {
        builder.setContentTitle(tasksQuantity.toString() + " " + context.getString(UiR.string.tasks_to_do))
        builder.setContentText(context.getString(UiR.string.touch_to_check))
    }

    private suspend fun initVibrate(builder: NotificationCompat.Builder) {
        if (getAlarmVibrateSettingUseCase()) {
            builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE or NotificationCompat.DEFAULT_LIGHTS)
        } else {
            builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun NotificationManager.makeNotificationChannel(context: Context) {
        createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID_ALARM,
                context.getString(UiR.string.notification_title_task_alarms),
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
