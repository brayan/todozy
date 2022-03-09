package br.com.sailboat.todozy.feature.task.list.impl.data.datasource

import br.com.sailboat.todozy.domain.model.TaskFilter
import br.com.sailboat.todozy.feature.task.list.impl.data.model.TaskData

interface TaskLocalDataSource {
    suspend fun getTask(taskId: Long): Result<TaskData>
    suspend fun getBeforeTodayTasks(filter: TaskFilter): Result<List<TaskData>>
    suspend fun getTodayTasks(filter: TaskFilter): Result<List<TaskData>>
    suspend fun getTomorrowTasks(filter: TaskFilter): Result<List<TaskData>>
    suspend fun getNextDaysTasks(filter: TaskFilter): Result<List<TaskData>>
    suspend fun getTasksThrowBeforeNow(): Result<List<TaskData>>
    suspend fun getTasksWithAlarms(): Result<List<TaskData>>
    suspend fun insert(taskData: TaskData): Result<Long>
    suspend fun update(taskData: TaskData, enabled: Boolean): Result<Unit?>
}