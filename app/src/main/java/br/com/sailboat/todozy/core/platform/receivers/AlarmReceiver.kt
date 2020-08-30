package br.com.sailboat.todozy.core.platform.receivers

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.extensions.log
import br.com.sailboat.todozy.core.extensions.safe
import br.com.sailboat.todozy.core.presentation.helper.NotificationHelper
import br.com.sailboat.todozy.features.LauncherActivity
import br.com.sailboat.todozy.features.settings.domain.usecase.GetAlarmSoundSetting
import br.com.sailboat.todozy.features.settings.domain.usecase.GetAlarmVibrateSetting
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.model.TaskCategory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskFilter
import br.com.sailboat.todozy.features.tasks.domain.usecase.tasks.GetTasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject

class AlarmReceiver : BroadcastReceiver(), KoinComponent {

    val getTasks: GetTasks by inject()
    val getAlarmSoundSetting: GetAlarmSoundSetting by inject()
    val getAlarmVibrateSetting: GetAlarmVibrateSetting by inject()

    override fun onReceive(context: Context, intent: Intent) {
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
        val notifyMgr = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifyMgr.notify(NotificationHelper().TASK_NOTIFICATION_ID, builder.build())
    }

    private suspend fun buildNotification(context: Context, intent: Intent): NotificationCompat.Builder {
        val resultIntent = Intent(context, LauncherActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context)
        builder.setSmallIcon(R.drawable.ic_vec_notification_icon)
        builder.setCategory(NotificationCompat.CATEGORY_ALARM)
        builder.priority = NotificationCompat.PRIORITY_HIGH
        builder.setContentIntent(resultPendingIntent)
        builder.setAutoCancel(true)
        builder.color = ContextCompat.getColor(context, R.color.md_blue_500)
        initContentTextAndTitle(context, intent, builder)
        initVibrate(builder)
        getAlarmSoundSetting()?.run { builder.setSound(this) }

        return builder
    }

    // TODO: REMOVE runBlocking
    private fun initContentTextAndTitle(context: Context, intent: Intent, builder: NotificationCompat.Builder) = runBlocking {
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

    private fun initContentFromIntent(intent: Intent, builder: NotificationCompat.Builder) {

        if (intent.hasExtra("EXTRA_TASK")) {
            val task = intent.getSerializableExtra("EXTRA_TASK") as Task
            builder.setContentTitle(task.name)

            if (task.notes?.isNotEmpty().safe()) {
                builder.setContentText(task.notes)
            }

        } else {
            builder.setContentTitle(intent.getStringExtra("NOME_TAREFA"))
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

//    private fun createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = getString(R.string.channel_name)
//            val descriptionText = getString(R.string.channel_description)
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
//                description = descriptionText
//            }
//            // Register the channel with the system
//            val notificationManager: NotificationManager =
//                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }

}