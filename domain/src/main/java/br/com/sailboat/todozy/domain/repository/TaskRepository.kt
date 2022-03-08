package br.com.sailboat.todozy.domain.repository

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskFilter

interface TaskRepository {
    suspend fun getTask(taskId: Long): Result<Task>
    suspend fun getBeforeTodayTasks(filter: TaskFilter): List<Task>
    suspend fun getTodayTasks(filter: TaskFilter): List<Task>
    suspend fun getTomorrowTasks(filter: TaskFilter): List<Task>
    suspend fun getNextDaysTasks(filter: TaskFilter): List<Task>
    suspend fun getBeforeNowTasks(): List<Task>
    suspend fun getTasksWithAlarms(): List<Task>
    suspend fun insert(task: Task): Result<Task>
    suspend fun update(task: Task): Result<Task>
    suspend fun disableTask(task: Task)
}