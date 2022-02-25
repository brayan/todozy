package br.com.sailboat.todozy.features.tasks.data.repository

import br.com.sailboat.todozy.features.tasks.data.datasource.local.AlarmLocalDataSource
import br.com.sailboat.todozy.features.tasks.data.mapper.AlarmDataToAlarmMapper
import br.com.sailboat.todozy.features.tasks.data.mapper.AlarmToAlarmDataMapper
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository

class AlarmRepositoryImpl(
    private val alarmLocalDataSource: AlarmLocalDataSource,
    private val alarmDataToAlarmMapper: AlarmDataToAlarmMapper,
    private val alarmToAlarmDataMapper: AlarmToAlarmDataMapper,
) : AlarmRepository {

    override suspend fun getAlarmByTaskId(taskId: Long): Alarm? {
        val alarmData = alarmLocalDataSource.getAlarmByTask(taskId)
        return alarmData?.run { alarmDataToAlarmMapper.map(this) }
    }

    override suspend fun deleteAlarmByTask(taskId: Long) {
        alarmLocalDataSource.deleteByTask(taskId)
    }

    override suspend fun save(alarm: Alarm, taskId: Long) {
        val alarmData = alarmToAlarmDataMapper.map(alarm, taskId)
        alarmLocalDataSource.save(alarmData)
    }

}