package br.com.sailboat.todozy.feature.alarm.impl.domain.service

import java.util.Calendar

interface AlarmManagerService {
    fun scheduleAlarm(dateTime: Calendar, taskId: Long)
    fun scheduleAlarmUpdates(calendar: Calendar)
    fun cancelAlarm(taskId: Long)
}
