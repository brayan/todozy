package br.com.sailboat.todozy.features.tasks.domain.repository

import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import java.util.*

interface AlarmRepository {
    suspend fun getAlarmByTaskId(taskId: Long): Alarm?
    suspend fun deleteAlarmByTask(taskId: Long)
    suspend fun save(alarm: Alarm, taskId: Long)
    suspend fun scheduleAlarm(alarm: Alarm, taskId: Long)
    suspend fun scheduleAlarmUpdates(calendar: Calendar)
    suspend fun cancelAlarmSchedule(taskId: Long)
}