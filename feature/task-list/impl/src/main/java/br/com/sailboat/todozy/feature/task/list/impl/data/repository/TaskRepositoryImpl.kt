package br.com.sailboat.todozy.feature.task.list.impl.data.repository

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskFilter
import br.com.sailboat.todozy.domain.repository.TaskRepository
import br.com.sailboat.todozy.feature.alarm.domain.repository.AlarmRepository
import br.com.sailboat.todozy.feature.task.list.impl.data.datasource.TaskLocalDataSource
import br.com.sailboat.todozy.feature.task.list.impl.data.model.TaskData
import br.com.sailboat.todozy.feature.task.list.impl.data.model.mapToTask
import br.com.sailboat.todozy.feature.task.list.impl.data.model.mapToTaskData

class TaskRepositoryImpl(
    private val alarmRepository: AlarmRepository,
    private val taskLocalDataSource: TaskLocalDataSource
) : TaskRepository {

    override suspend fun getTask(taskId: Long): Result<Task> = runCatching {
        val taskData = taskLocalDataSource.getTask(taskId).getOrThrow()
        val alarm = alarmRepository.getAlarmByTaskId(taskData.id).getOrNull()
        return@runCatching taskData.mapToTask(alarm)
    }

    override suspend fun getBeforeTodayTasks(filter: TaskFilter): Result<List<Task>> = runCatching {
        val tasksData = taskLocalDataSource.getBeforeTodayTasks(filter).getOrThrow()
        return@runCatching tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun getTodayTasks(filter: TaskFilter): Result<List<Task>> = runCatching {
        val tasksData = taskLocalDataSource.getTodayTasks(filter).getOrThrow()
        return@runCatching tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun getTomorrowTasks(filter: TaskFilter): Result<List<Task>> = runCatching {
        val tasksData = taskLocalDataSource.getTomorrowTasks(filter).getOrThrow()
        return@runCatching tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun getNextDaysTasks(filter: TaskFilter): Result<List<Task>> = runCatching {
        val tasksData = taskLocalDataSource.getNextDaysTasks(filter).getOrThrow()
        return@runCatching tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun getBeforeNowTasks(): Result<List<Task>> = runCatching {
        val tasksData = taskLocalDataSource.getTasksThrowBeforeNow().getOrThrow()
        return@runCatching tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun getTasksWithAlarms(): Result<List<Task>> = runCatching {
        val tasksData = taskLocalDataSource.getTasksWithAlarms().getOrThrow()
        return@runCatching tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun insert(task: Task): Result<Task> = runCatching {
        val taskData = task.mapToTaskData()
        val taskId = taskLocalDataSource.insert(taskData).getOrThrow()

        return@runCatching task.copy(id = taskId)
    }

    override suspend fun update(task: Task): Result<Task> = runCatching {
        taskLocalDataSource.update(task.mapToTaskData(), true)
        return@runCatching task
    }

    override suspend fun disableTask(task: Task): Result<Task> = runCatching {
        taskLocalDataSource.update(task.mapToTaskData(), false)
        return@runCatching task
    }

    private suspend fun List<TaskData>.loadAlarmsAndMapToTasks() = map { taskData ->
        val alarm = alarmRepository.getAlarmByTaskId(taskData.id).getOrNull()
        taskData.mapToTask(alarm)
    }
}
