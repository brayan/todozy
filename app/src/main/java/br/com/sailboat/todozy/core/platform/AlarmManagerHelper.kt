package br.com.sailboat.todozy.core.platform

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import br.com.sailboat.todozy.core.extensions.getInitialCalendarForTomorrow
import br.com.sailboat.todozy.core.extensions.incrementToNextValidDate
import br.com.sailboat.todozy.core.extensions.isBeforeNow
import br.com.sailboat.todozy.core.platform.receivers.AlarmReceiver
import br.com.sailboat.todozy.core.platform.receivers.AlarmUpdateReceiver
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import java.util.*

class AlarmManagerHelper(private val context: Context) {

    private var alarmIntent: Intent? = null
    private var pendingAlarmIntent: PendingIntent? = null
    private val alarmManager by lazy { (context.getSystemService(ALARM_SERVICE) as AlarmManager) }

    fun setNextValidAlarm(task: Task, alarm: Alarm) {
        initAlarmIntent(task)
        initPendingIntent(task)
        setNextValidAlarm(alarm)
    }

    private fun setNextValidAlarm(alarm: Alarm) {
        val nextAlarm = alarm.dateTime

        if (shouldSetNonRepetitiveAlarm(alarm)) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextAlarm.timeInMillis, pendingAlarmIntent)

        } else if (alarm.repeatType !== RepeatType.NOT_REPEAT) {

            if (nextAlarm.isBeforeNow()) {
                nextAlarm.incrementToNextValidDate(alarm.repeatType, alarm.customDays)
            }

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextAlarm.timeInMillis, pendingAlarmIntent)
        }

    }

    private fun shouldSetNonRepetitiveAlarm(alarm: Alarm): Boolean {
        return alarm.repeatType === RepeatType.NOT_REPEAT && alarm.dateTime.after(Calendar.getInstance())
    }

    private fun initPendingIntent(task: Task) {
        pendingAlarmIntent = PendingIntent.getBroadcast(context, task.id.toInt(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun initAlarmIntent(task: Task) {
        alarmIntent = Intent(context, AlarmReceiver::class.java)
        alarmIntent!!.putExtra("NOME_TAREFA", task.name)
        alarmIntent!!.putExtra("ID_TAREFA", task.id)
        // intentAlarme!!.putExtra("EXTRA_TASK", task)
    }


    fun setAlarmUpdateTasks() {
        val intent = Intent(context, AlarmUpdateReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context, 999999999, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getInitialCalendarForTomorrow().timeInMillis, AlarmManager.INTERVAL_DAY, alarmIntent)
    }

    fun cancelAlarm(task: Task) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("NOME_TAREFA", task.name)
        intent.putExtra("ID_TAREFA", task.id)

        val alarmIntent = PendingIntent.getBroadcast(context, task.id.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmIntent.cancel()
    }

}