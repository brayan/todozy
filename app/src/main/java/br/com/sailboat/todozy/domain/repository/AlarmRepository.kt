package br.com.sailboat.todozy.domain.repository

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.Task

interface AlarmRepository {
    suspend fun getAlarmByTaskId(taskId: Long): Alarm?
    suspend fun deleteAlarmByTask(task: Task)
    suspend fun save(alarm: Alarm, task: Task)
    suspend fun update(alarm: Alarm, task: Task)
    suspend fun setAlarm(alarm: Alarm, task: Task)
}