package br.com.sailboat.todozy.feature.task.history.impl.data.datasource

import br.com.sailboat.todozy.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.feature.task.history.impl.data.model.TaskHistoryData

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