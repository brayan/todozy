package br.com.sailboat.todozy.feature.task.history.impl.data.datasource

import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.feature.task.history.impl.data.model.TaskHistoryData

internal interface TaskHistoryLocalDataSource {
    fun getTodayHistory(filter: TaskHistoryFilter): Result<List<TaskHistoryData>>
    fun getYesterdayHistory(filter: TaskHistoryFilter): Result<List<TaskHistoryData>>
    fun getPreviousDaysHistory(filter: TaskHistoryFilter): Result<List<TaskHistoryData>>
    fun getTaskHistoryByTask(taskId: Long): Result<List<TaskHistoryData>>
    fun getTotalOfNotDoneTasks(filter: TaskHistoryFilter): Result<Int>
    fun getTotalOfDoneTasks(filter: TaskHistoryFilter): Result<Int>
    fun save(
        taskId: Long,
        taskStatus: Int,
    ): Result<Long>
    fun update(taskHistoryData: TaskHistoryData): Result<Unit?>
    fun delete(taskHistoryId: Long): Result<Unit?>
    fun deleteAllHistory(): Result<Unit?>
}
