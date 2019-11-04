package br.com.sailboat.todozy.data.repository

import android.content.Context
import br.com.sailboat.todozy.data.DatabaseOpenHelper
import br.com.sailboat.todozy.data.mapper.mapToAlarm
import br.com.sailboat.todozy.data.mapper.mapToAlarmData
import br.com.sailboat.todozy.data.mapper.mapToTask
import br.com.sailboat.todozy.data.mapper.mapToTaskData
import br.com.sailboat.todozy.data.model.TaskData
import br.com.sailboat.todozy.data.sqlite.AlarmSQLite
import br.com.sailboat.todozy.data.sqlite.TaskSQLite
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.repository.TaskRepository
import br.com.sailboat.todozy.domain.filter.TaskFilter

class TaskRepositoryImpl(database: DatabaseOpenHelper) : TaskRepository {

    private val taskDao by lazy { TaskSQLite(database) }
    private val alarmDao by lazy { AlarmSQLite(database) }

    override suspend fun getTask(taskId: Long): Task? {
        val taskData = taskDao.getTask(taskId) ?: return null
        val alarmData = alarmDao.getAlarmByTask(taskId)
        return taskData.mapToTask(alarmData?.mapToAlarm())
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
        taskDao.insert(task.mapToTaskData())
        task.alarm?.run { alarmDao.save(mapToAlarmData(task.id)) }
    }

    override suspend fun update(task: Task) {
        taskDao.update(task.mapToTaskData(), true)
        alarmDao.deleteByTask(task.id)

        task.alarm?.run { alarmDao.save(mapToAlarmData(task.id)) }
    }

    override suspend fun disableTask(task: Task) = taskDao.update(task.mapToTaskData(), false)

    private fun List<TaskData>.loadAlarmsAndMapToTasks() = map { taskData ->
        val alarmData = alarmDao.getAlarmByTask(taskData.id)
        taskData.mapToTask(alarmData?.mapToAlarm())
    }

}