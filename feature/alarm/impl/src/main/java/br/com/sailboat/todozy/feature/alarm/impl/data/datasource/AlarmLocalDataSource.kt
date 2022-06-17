package br.com.sailboat.todozy.feature.alarm.impl.data.datasource

import br.com.sailboat.todozy.feature.alarm.impl.data.model.AlarmData

interface AlarmLocalDataSource {
    fun getAlarmByTask(taskId: Long): Result<AlarmData?>
    fun deleteByTask(taskId: Long): Result<Unit?>
    fun update(alarmData: AlarmData): Result<Unit?>
    fun save(alarmData: AlarmData): Result<Long>
}
