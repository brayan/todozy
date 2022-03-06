package br.com.sailboat.todozy.feature.alarm.impl.data.datasource

import br.com.sailboat.todozy.feature.alarm.impl.data.model.AlarmData

interface AlarmLocalDataSource {
    fun getAlarmByTask(taskId: Long): AlarmData?
    fun deleteByTask(taskId: Long)
    fun update(alarmData: AlarmData)
    fun save(alarmData: AlarmData)
}