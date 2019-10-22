package br.com.sailboat.todozy.data.helper

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import br.com.sailboat.todozy.domain.helper.getInitialCalendarForTomorrow
import br.com.sailboat.todozy.domain.helper.incrementToNextValidDate
import br.com.sailboat.todozy.domain.helper.isBeforeNow
import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.ui.receivers.AlarmReceiver
import br.com.sailboat.todozy.ui.receivers.AlarmUpdateReceiver
import java.util.*

class AlarmManagerHelper(private val appContext: Context) {

    private var intentAlarme: Intent? = null
    private var pendingIntentAlarme: PendingIntent? = null
    private var alarmManager: AlarmManager? = null

    fun setNextValidAlarm(task: Task, alarm: Alarm) {
        inicializarComponentes(task)
        setNextValidAlarm(alarm)
    }

    private fun inicializarComponentes(task: Task) {
        inicializarIntentAlarme(task)
        inicializarPendingIntent(task)
        inicializarAlarmManager()
    }

    private fun setNextValidAlarm(alarm: Alarm) {

        val nextAlarm = alarm.dateTime

        if (shouldSetNonRepetitiveAlarm(alarm)) {

            alarmManager?.setExact(AlarmManager.RTC_WAKEUP, nextAlarm.timeInMillis, pendingIntentAlarme)


        } else if (alarm.repeatType !== RepeatType.NOT_REPEAT) {

            if (nextAlarm.isBeforeNow()) {
                nextAlarm.incrementToNextValidDate(alarm.repeatType, alarm.customDays)
            }

            alarmManager?.setExact(AlarmManager.RTC_WAKEUP, nextAlarm.timeInMillis, pendingIntentAlarme)
        }

    }

    private fun shouldSetNonRepetitiveAlarm(alarm: Alarm): Boolean {
        return alarm.repeatType === RepeatType.NOT_REPEAT && alarm.dateTime.after(Calendar.getInstance())
    }

    private fun inicializarAlarmManager() {
        alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    private fun inicializarPendingIntent(task: Task) {
        pendingIntentAlarme = PendingIntent.getBroadcast(appContext, task.id.toInt(), intentAlarme, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun inicializarIntentAlarme(task: Task) {
        intentAlarme = Intent(appContext, AlarmReceiver::class.java)
        intentAlarme!!.putExtra("NOME_TAREFA", task.name)
        intentAlarme!!.putExtra("ID_TAREFA", task.id)
        //intentAlarme!!.putExtra("EXTRA_TASK", task)
    }


    fun setAlarmUpdateTasks() {

        val intent = Intent(appContext, AlarmUpdateReceiver::class.java)

        val alarmIntent = PendingIntent.getBroadcast(appContext, 999999999, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        getAlarmManager().setRepeating(AlarmManager.RTC_WAKEUP, getInitialCalendarForTomorrow().timeInMillis, AlarmManager.INTERVAL_DAY, alarmIntent)
    }

    fun cancelAlarm(task: Task) {
        val intent = Intent(appContext, AlarmReceiver::class.java)
        intent.putExtra("NOME_TAREFA", task.name)
        intent.putExtra("ID_TAREFA", task.id)

        val alarmIntent = PendingIntent.getBroadcast(appContext, task.id.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmIntent.cancel()
    }

    fun getAlarmManager(): AlarmManager {
        return appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

}