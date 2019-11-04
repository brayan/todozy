package br.com.sailboat.todozy.data.repository

import android.content.Context
import br.com.sailboat.todozy.data.DatabaseOpenHelper
import br.com.sailboat.todozy.data.helper.AlarmManagerHelper
import br.com.sailboat.todozy.data.mapper.mapToAlarm
import br.com.sailboat.todozy.data.mapper.mapToAlarmData
import br.com.sailboat.todozy.data.sqlite.AlarmSQLite
import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.repository.AlarmRepository

class AlarmRepositoryImpl(database: DatabaseOpenHelper, context: Context) : AlarmRepository {

    private val alarmSQLite by lazy { AlarmSQLite(database) }
    private val alarmManager by lazy { AlarmManagerHelper(context) }

    override suspend fun getAlarmByTaskId(taskId: Long): Alarm? {
        val alarmData = this.alarmSQLite.getAlarmByTask(taskId)
        return alarmData?.mapToAlarm()
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