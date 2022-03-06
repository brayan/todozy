package br.com.sailboat.todozy.core.platform

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import br.com.sailboat.todozy.core.platform.receivers.AlarmReceiver
import br.com.sailboat.todozy.core.platform.receivers.ScheduleAlarmsReceiver
import br.com.sailboat.todozy.feature.alarm.impl.domain.service.AlarmManagerService
import java.util.*

class AlarmManagerServiceImpl(private val context: Context) : AlarmManagerService {

    private val alarmManager by lazy { (context.getSystemService(ALARM_SERVICE) as AlarmManager) }

    override fun scheduleAlarm(dateTime: Calendar, taskId: Long) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(AlarmReceiver.EXTRA_TASK_ID, taskId)

        val pending = PendingIntent.getBroadcast(
            context, taskId.toInt(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, dateTime.timeInMillis, pending)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                dateTime.timeInMillis,
                pending
            )
        } else {
            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(dateTime.timeInMillis, null),
                pending
            )
        }

    }

    override fun scheduleAlarmUpdates(calendar: Calendar) {
        val intent = Intent(context, ScheduleAlarmsReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(
            context, 999999999, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, alarmIntent
        )
    }

    override fun cancelAlarm(taskId: Long) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(AlarmReceiver.EXTRA_TASK_ID, taskId)

        val alarmIntent = PendingIntent.getBroadcast(
            context, taskId.toInt(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmIntent.cancel()

        alarmManager.cancel(alarmIntent)
    }

}