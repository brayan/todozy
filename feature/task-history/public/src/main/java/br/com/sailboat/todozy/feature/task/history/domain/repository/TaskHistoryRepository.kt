package br.com.sailboat.todozy.feature.task.history.domain.repository

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistory
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter

interface TaskHistoryRepository {
    suspend fun getTotalOfNotDoneTasks(filter: TaskHistoryFilter): Result<Int>
    suspend fun getTotalOfDoneTasks(filter: TaskHistoryFilter): Result<Int>
    suspend fun getTaskHistory(taskId: Long): Result<List<TaskHistory>>
    suspend fun getTodayHistory(filter: TaskHistoryFilter): Result<List<TaskHistory>>
    suspend fun getYesterdayHistory(filter: TaskHistoryFilter): Result<List<TaskHistory>>
    suspend fun getPreviousDaysHistory(filter: TaskHistoryFilter): Result<List<TaskHistory>>
    suspend fun insert(
        task: Task,
        status: TaskStatus,
    ): Result<Unit?>
    suspend fun update(taskHistory: TaskHistory): Result<Unit?>
    suspend fun delete(taskHistory: TaskHistory): Result<Unit?>
    suspend fun deleteAll(): Result<Unit?>
}
