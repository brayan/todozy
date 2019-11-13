package br.com.sailboat.todozy.data.repository

import br.com.sailboat.todozy.data.DatabaseOpenHelper
import br.com.sailboat.todozy.data.mapper.mapToAlarm
import br.com.sailboat.todozy.data.mapper.mapToAlarmData
import br.com.sailboat.todozy.data.mapper.mapToTask
import br.com.sailboat.todozy.data.mapper.mapToTaskData
import br.com.sailboat.todozy.data.model.TaskData
import br.com.sailboat.todozy.data.sqlite.AlarmSQLite
import br.com.sailboat.todozy.data.sqlite.TaskSQLite
import br.com.sailboat.todozy.domain.filter.TaskFilter
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.repository.AlarmRepository
import br.com.sailboat.todozy.domain.repository.TaskRepository

class TaskRepositoryImpl(private val alarmRepository: AlarmRepository, database: DatabaseOpenHelper) : TaskRepository {

    private val taskDao by lazy { TaskSQLite(database) }

    override suspend fun getTask(taskId: Long): Task? {
        val taskData = taskDao.getTask(taskId) ?: return null
        val alarm = alarmRepository.getAlarmByTaskId(taskData.id)
        return taskData.mapToTask(alarm)
    }

    override suspend fun getBeforeTodayTasks(filter: TaskFilter): List<Task> {
        val tasksData = taskDao.getBeforeTodayTasks(filter)
        return tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun getTodayTasks(filter: TaskFilter): List<Task> {
        val tasksData = taskDao.getTodayTasks(filter)
        return tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun getTomorrowTasks(filter: TaskFilter): List<Task> {
        val tasksData = taskDao.getTomorrowTasks(filter)
        return tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun getNextDaysTasks(filter: TaskFilter): List<Task> {
        val tasksData = taskDao.getNextDaysTasks(filter)
        return tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun getTasksThrowBeforeNow(): List<Task> {
        val tasksData = taskDao.getTasksThrowBeforeNow()
        return tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun getTasksWithAlarms(): List<Task> {
        val tasksData = taskDao.getTasksWithAlarms()
        return tasksData.loadAlarmsAndMapToTasks()
    }

    override suspend fun insert(task: Task) {
        task.id = taskDao.insert(task.mapToTaskData())
        task.alarm?.run { alarmRepository.save(this, task) }
    }

    override suspend fun update(task: Task) {
        taskDao.update(task.mapToTaskData(), true)
        alarmRepository.deleteAlarmByTask(task)
        task.alarm?.run { alarmRepository.save(this, task) }
    }

    override suspend fun disableTask(task: Task) = taskDao.update(task.mapToTaskData(), false)

    private suspend fun List<TaskData>.loadAlarmsAndMapToTasks() = map { taskData ->
        val alarm = alarmRepository.getAlarmByTaskId(taskData.id)
        taskData.mapToTask(alarm)
    }

}