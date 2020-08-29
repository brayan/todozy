package br.com.sailboat.todozy.features.tasks.domain.repository

import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.Task

interface AlarmRepository {
    suspend fun getAlarmByTaskId(taskId: Long): Alarm?
    suspend fun deleteAlarmByTask(task: Task)
    suspend fun save(alarm: Alarm, task: Task)
    suspend fun update(alarm: Alarm, task: Task)
    suspend fun setAlarm(alarm: Alarm, task: Task)
    suspend fun getNextValidAlarm(alarm: Alarm): Alarm
}