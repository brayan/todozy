package br.com.sailboat.todozy.feature.alarm.domain.repository

import br.com.sailboat.todozy.domain.model.Alarm

interface AlarmRepository {
    suspend fun getAlarmByTaskId(taskId: Long): Result<Alarm?>
    suspend fun deleteAlarmByTask(taskId: Long): Result<Unit?>
    suspend fun save(
        alarm: Alarm,
        taskId: Long,
    ): Result<Unit?>
}
