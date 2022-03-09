package br.com.sailboat.todozy.domain.repository

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskFilter

interface TaskRepository {
    suspend fun getTask(taskId: Long): Result<Task>
    suspend fun getBeforeTodayTasks(filter: TaskFilter): Result<List<Task>>
    suspend fun getTodayTasks(filter: TaskFilter): Result<List<Task>>
    suspend fun getTomorrowTasks(filter: TaskFilter): Result<List<Task>>
    suspend fun getNextDaysTasks(filter: TaskFilter): Result<List<Task>>
    suspend fun getBeforeNowTasks(): Result<List<Task>>
    suspend fun getTasksWithAlarms(): Result<List<Task>>
    suspend fun insert(task: Task): Result<Task>
    suspend fun update(task: Task): Result<Task>
    suspend fun disableTask(task: Task): Result<Task>
}