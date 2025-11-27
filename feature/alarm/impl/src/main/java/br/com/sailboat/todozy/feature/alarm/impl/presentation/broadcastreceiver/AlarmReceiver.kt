
package br.com.sailboat.todozy.feature.alarm.impl.presentation.broadcastreceiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import br.com.sailboat.todozy.domain.model.Task
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
import br.com.sailboat.uicomponent.impl.helper.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.DateFormat
import br.com.sailboat.uicomponent.impl.R as UiR

internal class AlarmReceiver : BroadcastReceiver(), KoinComponent {
    private val getTaskUseCase: GetTaskUseCase by inject()
    private val getTasksUseCase: GetTasksUseCase by inject()
    private val getAlarmSoundSettingUseCase: GetAlarmSoundSettingUseCase by inject()
    private val getAlarmVibrateSettingUseCase: GetAlarmVibrateSettingUseCase by inject()
    private val splashNavigator: SplashNavigator by inject()

    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        "${javaClass.simpleName}.onReceive()".logDebug()
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val taskId = intent.getLongExtra(EXTRA_TASK_ID, -1)
                if (taskId == -1L) return@launch

                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                ensureNotificationChannel(context, notificationManager)

                val tasks = loadDueTasks()
                val task = getTaskUseCase(taskId).getOrThrow()

                val builder = buildNotification(context, task)
                notificationManager.notify(NotificationHelper.notificationIdFor(taskId), builder.build())

                if (tasks.size > 1) {
                    val summaryBuilder = buildSummaryNotification(context, tasks)
                    notificationManager.notify(NotificationHelper.TASK_NOTIFICATION_SUMMARY_ID, summaryBuilder.build())
                } else {
                    notificationManager.cancel(NotificationHelper.TASK_NOTIFICATION_SUMMARY_ID)
                }
            } catch (e: Exception) {
                e.log()
            } finally {
                pendingResult.finish()
            }
        }
    }

    private suspend fun loadDueTasks(): List<Task> {
        val taskFilter = TaskFilter(category = TaskCategory.BEFORE_NOW)
        return getTasksUseCase(taskFilter).getOrDefault(emptyList())
    }

    private suspend fun buildNotification(
        context: Context,
        task: Task,
    ): NotificationCompat.Builder {
        val resultIntent = splashNavigator.getSplashIntent(context)
        val resultPendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                resultIntent,
                getPendingIntentFlags(),
            )

        val builder =
            NotificationCompat.Builder(context, CHANNEL_ID_ALARM)
                .setSmallIcon(UiR.drawable.ic_vec_notification_icon)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setWhen(task.alarm?.dateTime?.timeInMillis ?: System.currentTimeMillis())
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .apply { priority = NotificationCompat.PRIORITY_HIGH }
                .apply { color = ContextCompat.getColor(context, UiR.color.md_blue_500) }
                .setGroup(NotificationHelper.TASK_NOTIFICATION_GROUP_KEY)

        val contentText = task.notes?.takeIf { it.isNotBlank() } ?: context.getString(UiR.string.touch_to_check)
        builder.setContentTitle(task.name)
        builder.setContentText(contentText)
        builder.setSubText(task.dueTimeText(context))
        builder.setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
        builder.addAction(buildMarkDoneAction(context, task.id))
        builder.addAction(buildSnoozeAction(context, task.id))

        applyLegacyNotificationSettings(builder)

        return builder
    }

    private fun buildSummaryNotification(
        context: Context,
        tasks: List<Task>,
    ): NotificationCompat.Builder {
        val total = tasks.size
        val inboxStyle = NotificationCompat.InboxStyle()
        tasks.take(5).forEach { inboxStyle.addLine(it.name) }

        return NotificationCompat.Builder(context, CHANNEL_ID_ALARM)
            .setSmallIcon(UiR.drawable.ic_vec_notification_icon)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentTitle(
                context.resources.getQuantityString(
                    UiR.plurals.notification_tasks_due,
                    total,
                    total,
                ),
            )
            .setContentText(context.getString(UiR.string.touch_to_check))
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setStyle(inboxStyle)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    splashNavigator.getSplashIntent(context),
                    getPendingIntentFlags(),
                ),
            )
            .setGroup(NotificationHelper.TASK_NOTIFICATION_GROUP_KEY)
            .setGroupSummary(true)
            .setColor(ContextCompat.getColor(context, UiR.color.md_blue_500))
    }

    private fun Task.dueTimeText(context: Context): String? =
        alarm?.dateTime?.time?.let { DateFormat.getTimeInstance(DateFormat.SHORT).format(it) }

    private fun buildMarkDoneAction(
        context: Context,
        taskId: Long,
    ): NotificationCompat.Action {
        val intent =
            Intent(context, NotificationActionReceiver::class.java).apply {
                action = NotificationActionReceiver.ACTION_MARK_DONE
                putExtra(EXTRA_TASK_ID, taskId)
            }
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                requestCodeForAction(taskId, NotificationActionReceiver.ACTION_MARK_DONE),
                intent,
                getPendingIntentFlags(),
            )
        return NotificationCompat.Action(
            UiR.drawable.ic_check_white_24dp,
            context.getString(UiR.string.notification_action_mark_done),
            pendingIntent,
        )
    }

    private fun buildSnoozeAction(
        context: Context,
        taskId: Long,
    ): NotificationCompat.Action {
        val intent =
            Intent(context, NotificationActionReceiver::class.java).apply {
                action = NotificationActionReceiver.ACTION_SNOOZE
                putExtra(EXTRA_TASK_ID, taskId)
            }
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                requestCodeForAction(taskId, NotificationActionReceiver.ACTION_SNOOZE),
                intent,
                getPendingIntentFlags(),
            )
        return NotificationCompat.Action(
            0,
            context.getString(UiR.string.notification_action_snooze),
            pendingIntent,
        )
    }

    private fun requestCodeForAction(
        taskId: Long,
        action: String,
    ) = NotificationHelper.notificationIdFor(taskId) + action.hashCode()

    private suspend fun applyLegacyNotificationSettings(builder: NotificationCompat.Builder) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            if (getAlarmVibrateSettingUseCase()) {
                builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE or NotificationCompat.DEFAULT_LIGHTS)
            } else {
                builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS)
            }
            getAlarmSoundSettingUseCase()?.run { builder.setSound(this) }
        }
    }

    private suspend fun ensureNotificationChannel(
        context: Context,
        notificationManager: NotificationManager,
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val sound = getAlarmSoundSettingUseCase()
        val shouldVibrate = getAlarmVibrateSettingUseCase()
        val existingChannel = notificationManager.getNotificationChannel(CHANNEL_ID_ALARM)

        val needsUpdate =
            existingChannel?.let { channel ->
                val soundChanged = channel.sound != sound
                val vibrationChanged = channel.shouldVibrate() != shouldVibrate
                soundChanged || vibrationChanged
            } ?: true

        if (needsUpdate && existingChannel != null) {
            notificationManager.deleteNotificationChannel(CHANNEL_ID_ALARM)
        }

        if (needsUpdate) {
            notificationManager.makeNotificationChannel(context, sound, shouldVibrate)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun NotificationChannel.shouldVibrate(): Boolean =
        vibrationPattern != null && vibrationPattern.isNotEmpty() && vibrationPattern.any { it != 0L }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun NotificationManager.makeNotificationChannel(
        context: Context,
        sound: android.net.Uri?,
        shouldVibrate: Boolean,
    ) {
        createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID_ALARM,
                context.getString(UiR.string.notification_title_task_alarms),
                NotificationManager.IMPORTANCE_HIGH,
            ).apply {
                description = context.getString(UiR.string.notification_channel_description_task_alarms)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                enableLights(true)
                enableVibration(shouldVibrate)
                vibrationPattern = if (shouldVibrate) longArrayOf(0, 250, 250, 250) else null
                val attributes =
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                setSound(sound, attributes)
            },
        )
    }

    companion object {
        const val EXTRA_TASK_ID = "EXTRA_TASK_ID"
        const val CHANNEL_ID_ALARM = "CHANNEL_ID_ALARM"
    }
}
