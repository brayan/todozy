package br.com.sailboat.todozy.features.tasks.data.datasource.local

import br.com.sailboat.todozy.features.tasks.data.model.TaskHistoryData
import br.com.sailboat.todozy.domain.model.TaskHistoryFilter

interface TaskHistoryLocalDataSource {
    fun getTodayHistory(filter: TaskHistoryFilter): List<TaskHistoryData>
    fun getYesterdayHistory(filter: TaskHistoryFilter): List<TaskHistoryData>
    fun getPreviousDaysHistory(filter: TaskHistoryFilter): List<TaskHistoryData>
    fun save(taskId: Long, taskStatus: Int): Long
    fun update(taskHistoryData: TaskHistoryData)
    fun delete(taskHistoryId: Long)
    fun deleteAllHistory()
    fun getTotalOfNotDoneTasks(filter: TaskHistoryFilter): Int
    fun getTotalOfDoneTasks(filter: TaskHistoryFilter): Int
    fun getTaskHistoryByTask(taskId: Long): List<TaskHistoryData>
}