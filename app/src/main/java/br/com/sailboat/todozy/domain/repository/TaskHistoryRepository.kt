package br.com.sailboat.todozy.domain.repository

import br.com.sailboat.todozy.domain.filter.TaskHistoryFilter
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskHistory
import br.com.sailboat.todozy.domain.model.TaskStatus

interface TaskHistoryRepository {
    suspend fun getTotalOfNotDoneTasks(filter: TaskHistoryFilter): Int
    suspend fun getTotalOfDoneTasks(filter: TaskHistoryFilter): Int
    suspend fun getTaskHistory(taskId: Long): List<TaskHistory>
    suspend fun getTodayHistory(filter: TaskHistoryFilter): List<TaskHistory>
    suspend fun getYesterdayHistory(filter: TaskHistoryFilter): List<TaskHistory>
    suspend fun getPreviousDaysHistory(filter: TaskHistoryFilter): List<TaskHistory>
    suspend fun insert(task: Task, status: TaskStatus)
    suspend fun update(taskHistory: TaskHistory)
    suspend fun delete(taskHistory: TaskHistory)
    suspend fun deleteAll()
}