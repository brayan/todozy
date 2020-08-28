package br.com.sailboat.todozy.features.tasks.domain.repository

import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.model.TaskFilter

interface TaskRepository {
    suspend fun getTask(taskId: Long): Task
    suspend fun getBeforeTodayTasks(filter: TaskFilter): List<Task>
    suspend fun getTodayTasks(filter: TaskFilter): List<Task>
    suspend fun getTomorrowTasks(filter: TaskFilter): List<Task>
    suspend fun getNextDaysTasks(filter: TaskFilter): List<Task>
    suspend fun getBeforeNowTasks(): List<Task>
    suspend fun getTasksWithAlarms(): List<Task>
    suspend fun insert(task: Task)
    suspend fun update(task: Task)
    suspend fun disableTask(task: Task)
}