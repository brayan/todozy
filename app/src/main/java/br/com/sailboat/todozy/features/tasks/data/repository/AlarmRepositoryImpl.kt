package br.com.sailboat.todozy.features.tasks.data.repository

import br.com.sailboat.todozy.core.extensions.logDebug
import br.com.sailboat.todozy.features.tasks.data.datasource.local.AlarmLocalDataSource
import br.com.sailboat.todozy.features.tasks.data.model.mapToAlarm
import br.com.sailboat.todozy.features.tasks.data.model.mapToAlarmData
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository

class AlarmRepositoryImpl(
    private val alarmLocalDataSource: AlarmLocalDataSource,
) : AlarmRepository {

    override suspend fun getAlarmByTaskId(taskId: Long): Alarm? {
        "${javaClass.simpleName}.getAlarmByTaskId(taskId: $taskId)".logDebug()
        return alarmLocalDataSource.getAlarmByTask(taskId)?.mapToAlarm()
    }

    override suspend fun deleteAlarmByTask(taskId: Long) {
        "${javaClass.simpleName}.deleteAlarmByTask(taskId: $taskId)".logDebug()
        alarmLocalDataSource.deleteByTask(taskId)
    }

    override suspend fun save(alarm: Alarm, taskId: Long) {
        "${javaClass.simpleName}.save($alarm, taskId: $taskId)".logDebug()
        alarmLocalDataSource.save(alarm.mapToAlarmData(taskId))
    }

}