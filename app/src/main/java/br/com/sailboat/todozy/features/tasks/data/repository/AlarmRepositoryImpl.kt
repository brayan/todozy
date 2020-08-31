package br.com.sailboat.todozy.features.tasks.data.repository

import br.com.sailboat.todozy.core.extensions.logDebug
import br.com.sailboat.todozy.core.platform.AlarmManagerHelper
import br.com.sailboat.todozy.features.tasks.data.datasource.local.AlarmLocalDataSource
import br.com.sailboat.todozy.features.tasks.data.model.mapToAlarm
import br.com.sailboat.todozy.features.tasks.data.model.mapToAlarmData
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository
import java.util.*

class AlarmRepositoryImpl(private val alarmLocalDataSource: AlarmLocalDataSource,
                          private val alarmManager: AlarmManagerHelper) : AlarmRepository {

    override suspend fun getAlarmByTaskId(taskId: Long): Alarm? {
        "${javaClass.simpleName}.getAlarmByTaskId($taskId)".logDebug()
        return alarmLocalDataSource.getAlarmByTask(taskId)?.mapToAlarm()
    }

    override suspend fun deleteAlarmByTask(taskId: Long) {
        "${javaClass.simpleName}.deleteAlarmByTask($taskId)".logDebug()
        alarmLocalDataSource.deleteByTask(taskId)
    }

    override suspend fun save(alarm: Alarm, taskId: Long) {
        "${javaClass.simpleName}.save($alarm , $taskId)".logDebug()
        alarmLocalDataSource.save(alarm.mapToAlarmData(taskId))
    }

    override suspend fun scheduleAlarm(alarm: Alarm, taskId: Long) {
        "${javaClass.simpleName}.scheduleAlarm($alarm , $taskId)".logDebug()
        alarmManager.scheduleAlarm(alarm.dateTime, taskId)
    }

    override suspend fun scheduleAlarmUpdates(calendar: Calendar) {
        "${javaClass.simpleName}.scheduleAlarmUpdates($calendar)".logDebug()
        alarmManager.scheduleAlarmUpdates(calendar)
    }

    override suspend fun cancelAlarmSchedule(taskId: Long) {
        "${javaClass.simpleName}.cancelAlarmSchedule($taskId)".logDebug()
        alarmManager.cancelAlarm(taskId)
    }

}