package br.com.sailboat.todozy.feature.alarm.impl.domain.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import br.com.sailboat.todozy.feature.alarm.impl.presentation.broadcastreceiver.AlarmReceiver
import br.com.sailboat.todozy.feature.alarm.impl.presentation.broadcastreceiver.ScheduleAlarmsReceiver
import br.com.sailboat.todozy.utility.android.intent.getPendingIntentFlags
import java.util.Calendar

internal class AlarmManagerServiceImpl(private val context: Context) : AlarmManagerService {

    private val alarmManager by lazy { (context.getSystemService(ALARM_SERVICE) as AlarmManager) }

    override fun scheduleAlarm(dateTime: Calendar, taskId: Long) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(AlarmReceiver.EXTRA_TASK_ID, taskId)

        val pending = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            intent,
            getPendingIntentFlags()
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
            context,
            999999999,
            intent,
            getPendingIntentFlags(),
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
            getPendingIntentFlags()
        )
        alarmIntent.cancel()

        alarmManager.cancel(alarmIntent)
    }
}
