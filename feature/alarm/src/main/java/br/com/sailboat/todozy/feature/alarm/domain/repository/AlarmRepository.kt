package br.com.sailboat.todozy.feature.alarm.domain.repository

import br.com.sailboat.todozy.domain.model.Alarm

interface AlarmRepository {
    suspend fun getAlarmByTaskId(taskId: Long): Alarm?
    suspend fun deleteAlarmByTask(taskId: Long)
    suspend fun save(alarm: Alarm, taskId: Long)
}