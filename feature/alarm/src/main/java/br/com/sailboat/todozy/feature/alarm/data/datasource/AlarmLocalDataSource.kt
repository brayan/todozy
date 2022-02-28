package br.com.sailboat.todozy.feature.alarm.data.datasource

import br.com.sailboat.todozy.feature.alarm.data.model.AlarmData

interface AlarmLocalDataSource {
    fun getAlarmByTask(taskId: Long): AlarmData?
    fun deleteByTask(taskId: Long)
    fun update(alarmData: AlarmData)
    fun save(alarmData: AlarmData)
}