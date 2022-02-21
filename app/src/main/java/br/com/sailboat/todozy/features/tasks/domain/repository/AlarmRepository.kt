package br.com.sailboat.todozy.features.tasks.domain.repository

import br.com.sailboat.todozy.features.tasks.domain.model.Alarm

interface AlarmRepository {
    suspend fun getAlarmByTaskId(taskId: Long): Alarm?
    suspend fun deleteAlarmByTask(taskId: Long)
    suspend fun save(alarm: Alarm, taskId: Long)
}