package br.com.sailboat.todozy.ui.receivers

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.data.DatabaseOpenHelper
import br.com.sailboat.todozy.data.helper.PreferencesHelper
import br.com.sailboat.todozy.data.sqlite.TaskSQLite
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.ui.LauncherActivity
import br.com.sailboat.todozy.ui.helper.NotificationHelper
import br.com.sailboat.todozy.ui.helper.log
import org.koin.core.KoinComponent
import org.koin.core.inject

class AlarmReceiver : BroadcastReceiver(), KoinComponent {

    val databaseOpenHelper: DatabaseOpenHelper by inject()

    override fun onReceive(context: Context?, intent: Intent?) {

        if (context != null && intent != null) {
            val builder = buildNotification(context, intent)
            throwNotification(context, builder)
        }
    }

    private fun throwNotification(context: Context, builder: NotificationCompat.Builder) {
        val notifyMgr = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifyMgr.notify(NotificationHelper().TASK_NOTIFICATION_ID, builder.build())
    }

    private fun buildNotification(context: Context, intent: Intent): NotificationCompat.Builder {
        val resultIntent = Intent(context, LauncherActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)


        val builder = NotificationCompat.Builder(context)
        builder.setSmallIcon(R.drawable.notification_icon)
        builder.setCategory(NotificationCompat.CATEGORY_ALARM)
        builder.priority = NotificationCompat.PRIORITY_HIGH
        builder.setContentIntent(resultPendingIntent)
        builder.setAutoCancel(true)
        builder.color = ContextCompat.getColor(context, R.color.md_blue_500)
        initContentTextAndTitle(context, intent, builder)
        initVibrate(context, builder)
        initSound(context, builder)

        return builder
    }

    private fun initContentTextAndTitle(context: Context, intent: Intent, builder: NotificationCompat.Builder) {

        try {
            val tasks = TaskSQLite(databaseOpenHelper).getTasksThrowBeforeNow()

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

            if (!TextUtils.isEmpty(task.notes)) {
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

    private fun initVibrate(context: Context, builder: NotificationCompat.Builder) {
        if (PreferencesHelper(context).isVibrationSettingAllowed()) {
            builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE or NotificationCompat.DEFAULT_LIGHTS)
        } else {
            builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS)
        }
    }

    private fun initSound(context: Context, builder: NotificationCompat.Builder) {
        builder.setSound(PreferencesHelper(context).getCurrentNotificationSound())
    }

}