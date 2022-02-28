package br.com.sailboat.todozy.features.tasks.data.repository

import br.com.sailboat.todozy.features.tasks.data.datasource.local.TaskLocalDataSource
import br.com.sailboat.todozy.features.tasks.data.model.TaskData
import br.com.sailboat.todozy.features.tasks.data.model.mapToTask
import br.com.sailboat.todozy.features.tasks.data.model.mapToTaskData
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.model.TaskFilter
import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskRepository
import br.com.sailboat.todozy.utility.android.log.logDebug

class TaskRepositoryImpl(
    private val alarmRepository: AlarmRepository,
    private val taskLocalDataSource: TaskLocalDataSource
) : TaskRepository {

    override suspend fun getTask(taskId: Long): Task {
        "${javaClass.simpleName}.getTask($taskId)".logDebug()

        val taskData = taskLocalDataSource.getTask(taskId)
        val alarm = alarmRepository.getAlarmByTaskId(taskData.id)
        return taskData.mapToTask(alarm)
    }

    override suspend fun getBeforeTodayTasks(filter: TaskFilter): List<Task> {
        "${javaClass.simpleName}.getBeforeTodayTasks($filter)".logDebug()
        val tasksData = taskLocalDataSource.getBeforeTodayTasks(filter)
        return tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun getTodayTasks(filter: TaskFilter): List<Task> {
        "${javaClass.simpleName}.getTodayTasks($filter)".logDebug()
        val tasksData = taskLocalDataSource.getTodayTasks(filter)
        return tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun getTomorrowTasks(filter: TaskFilter): List<Task> {
        "${javaClass.simpleName}.getTomorrowTasks($filter)".logDebug()
        val tasksData = taskLocalDataSource.getTomorrowTasks(filter)
        return tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun getNextDaysTasks(filter: TaskFilter): List<Task> {
        "${javaClass.simpleName}.getNextDaysTasks($filter)".logDebug()
        val tasksData = taskLocalDataSource.getNextDaysTasks(filter)
        return tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun getBeforeNowTasks(): List<Task> {
        "${javaClass.simpleName}.getBeforeNowTasks()".logDebug()
        val tasksData = taskLocalDataSource.getTasksThrowBeforeNow()
        return tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun getTasksWithAlarms(): List<Task> {
        "${javaClass.simpleName}.getTasksWithAlarms()".logDebug()
        val tasksData = taskLocalDataSource.getTasksWithAlarms()
        return tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun insert(task: Task) {
        "${javaClass.simpleName}.insert($task)".logDebug()
        task.id = taskLocalDataSource.insert(task.mapToTaskData())
    }

    override suspend fun update(task: Task) {
        "${javaClass.simpleName}.update($task)".logDebug()
        taskLocalDataSource.update(task.mapToTaskData(), true)
    }

    override suspend fun disableTask(task: Task) {
        "${javaClass.simpleName}.disableTask($task)".logDebug()
        taskLocalDataSource.update(task.mapToTaskData(), false)
    }

    private suspend fun List<TaskData>.loadAlarmsAndMapToTasks() = map { taskData ->
        val alarm = alarmRepository.getAlarmByTaskId(taskData.id)
        taskData.mapToTask(alarm)
    }

}