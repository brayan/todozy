package br.com.sailboat.todozy.features.tasks.data.repository

import android.content.Context
import br.com.sailboat.todozy.core.platform.AlarmManagerHelper
import br.com.sailboat.todozy.core.platform.DatabaseOpenHelper
import br.com.sailboat.todozy.features.tasks.data.datasource.local.AlarmLocalDataSourceSQLite
import br.com.sailboat.todozy.features.tasks.data.model.mapToAlarm
import br.com.sailboat.todozy.features.tasks.data.model.mapToAlarmData
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository

class AlarmRepositoryImpl(database: DatabaseOpenHelper, context: Context) : AlarmRepository {

    private val alarmSQLite by lazy { AlarmLocalDataSourceSQLite(database) }
    private val alarmManager by lazy { AlarmManagerHelper(context) }

    override suspend fun getAlarmByTaskId(taskId: Long): Alarm? {
        return alarmSQLite.getAlarmByTask(taskId)?.mapToAlarm()
    }

    override suspend fun deleteAlarmByTask(task: Task) {
        alarmSQLite.deleteByTask(task.id)
        alarmManager.cancelAlarm(task)
    }

    override suspend fun update(alarm: Alarm, task: Task) {
        alarmSQLite.update(alarm.mapToAlarmData(task.id))
        alarmManager.cancelAlarm(task)
        alarmManager.setNextValidAlarm(task, alarm)
    }

    override suspend fun save(alarm: Alarm, task: Task) {
        alarmSQLite.save(alarm.mapToAlarmData(task.id))
        alarmManager.cancelAlarm(task)
        alarmManager.setNextValidAlarm(task, alarm)
    }

    override suspend fun setAlarm(alarm: Alarm, task: Task) {
        alarmManager.setNextValidAlarm(task, alarm)
    }

}