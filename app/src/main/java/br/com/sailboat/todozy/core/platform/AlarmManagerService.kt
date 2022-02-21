package br.com.sailboat.todozy.core.platform

import java.util.*

interface AlarmManagerService {
    fun scheduleAlarm(dateTime: Calendar, taskId: Long)
    fun scheduleAlarmUpdates(calendar: Calendar)
    fun cancelAlarm(taskId: Long)
}