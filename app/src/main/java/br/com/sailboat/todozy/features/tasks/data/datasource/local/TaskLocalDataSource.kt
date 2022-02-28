package br.com.sailboat.todozy.features.tasks.data.datasource.local

import br.com.sailboat.todozy.features.tasks.data.model.TaskData
import br.com.sailboat.todozy.domain.model.TaskFilter

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