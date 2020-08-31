package br.com.sailboat.todozy.features.tasks.data.datasource.local

import br.com.sailboat.todozy.features.tasks.data.model.AlarmData

interface AlarmLocalDataSource {
    fun getAlarmByTask(taskId: Long): AlarmData?
    fun deleteByTask(taskId: Long)
    fun update(alarmData: AlarmData)
    fun save(alarmData: AlarmData)
}