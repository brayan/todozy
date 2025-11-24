package br.com.sailboat.todozy.feature.alarm.impl.data.repository

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.feature.alarm.domain.repository.AlarmRepository
import br.com.sailboat.todozy.feature.alarm.impl.data.datasource.AlarmLocalDataSource
import br.com.sailboat.todozy.feature.alarm.impl.data.mapper.AlarmDataToAlarmMapper
import br.com.sailboat.todozy.feature.alarm.impl.data.mapper.AlarmToAlarmDataMapper

internal class AlarmRepositoryImpl(
    private val alarmLocalDataSource: AlarmLocalDataSource,
    private val alarmDataToAlarmMapper: AlarmDataToAlarmMapper,
    private val alarmToAlarmDataMapper: AlarmToAlarmDataMapper,
) : AlarmRepository {
    override suspend fun getAlarmByTaskId(taskId: Long): Result<Alarm?> =
        runCatching {
            val alarmData = alarmLocalDataSource.getAlarmByTask(taskId).getOrNull()
            return@runCatching alarmData?.run { alarmDataToAlarmMapper.map(this) }
        }

    override suspend fun deleteAlarmByTask(taskId: Long): Result<Unit?> {
        return alarmLocalDataSource.deleteByTask(taskId)
    }

    override suspend fun save(
        alarm: Alarm,
        taskId: Long,
    ): Result<Unit?> =
        runCatching {
            val alarmData = alarmToAlarmDataMapper.map(alarm, taskId)
            alarmLocalDataSource.save(alarmData).getOrThrow()
            return@runCatching
        }
}
