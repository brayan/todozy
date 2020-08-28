package br.com.sailboat.todozy.features.tasks.data.repository

import br.com.sailboat.todozy.features.tasks.data.datasource.local.TaskLocalDataSource
import br.com.sailboat.todozy.features.tasks.data.model.TaskData
import br.com.sailboat.todozy.features.tasks.data.model.mapToTask
import br.com.sailboat.todozy.features.tasks.data.model.mapToTaskData
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.model.TaskFilter
import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskRepository

class TaskRepositoryImpl(private val alarmRepository: AlarmRepository,
                         private val taskLocalDataSource: TaskLocalDataSource) : TaskRepository {

    override suspend fun getTask(taskId: Long): Task {
        val taskData = taskLocalDataSource.getTask(taskId)
        val alarm = alarmRepository.getAlarmByTaskId(taskData.id)
        return taskData.mapToTask(alarm)
    }

    override suspend fun getBeforeTodayTasks(filter: TaskFilter): List<Task> {
        val tasksData = taskLocalDataSource.getBeforeTodayTasks(filter)
        return tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun getTodayTasks(filter: TaskFilter): List<Task> {
        val tasksData = taskLocalDataSource.getTodayTasks(filter)
        return tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun getTomorrowTasks(filter: TaskFilter): List<Task> {
        val tasksData = taskLocalDataSource.getTomorrowTasks(filter)
        return tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun getNextDaysTasks(filter: TaskFilter): List<Task> {
        val tasksData = taskLocalDataSource.getNextDaysTasks(filter)
        return tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun getBeforeNowTasks(): List<Task> {
        val tasksData = taskLocalDataSource.getTasksThrowBeforeNow()
        return tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun getTasksWithAlarms(): List<Task> {
        val tasksData = taskLocalDataSource.getTasksWithAlarms()
        return tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun insert(task: Task) {
        task.id = taskLocalDataSource.insert(task.mapToTaskData())
        task.alarm?.run { alarmRepository.save(this, task) }
    }

    override suspend fun update(task: Task) {
        taskLocalDataSource.update(task.mapToTaskData(), true)
        alarmRepository.deleteAlarmByTask(task)
        task.alarm?.run { alarmRepository.save(this, task) }
    }

    override suspend fun disableTask(task: Task) = taskLocalDataSource.update(task.mapToTaskData(), false)

    private suspend fun List<TaskData>.loadAlarmsAndMapToTasks() = map { taskData ->
        val alarm = alarmRepository.getAlarmByTaskId(taskData.id)
        taskData.mapToTask(alarm)
    }

}