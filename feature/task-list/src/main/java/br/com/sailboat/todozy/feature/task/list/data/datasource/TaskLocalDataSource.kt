package br.com.sailboat.todozy.feature.task.list.data.datasource

import br.com.sailboat.todozy.domain.model.TaskFilter
import br.com.sailboat.todozy.feature.task.list.data.model.TaskData

interface TaskLocalDataSource {
    suspend fun getTask(taskId: Long): TaskData
    suspend fun getBeforeTodayTasks(filter: TaskFilter): List<TaskData>
    suspend fun getTodayTasks(filter: TaskFilter): List<TaskData>
    suspend fun getTomorrowTasks(filter: TaskFilter): List<TaskData>
    suspend fun getNextDaysTasks(filter: TaskFilter): List<TaskData>
    suspend fun getTasksThrowBeforeNow(): List<TaskData>
    suspend fun getTasksWithAlarms(): List<TaskData>
    suspend fun insert(taskData: TaskData): Long
    suspend fun update(taskData: TaskData, enabled: Boolean)
}