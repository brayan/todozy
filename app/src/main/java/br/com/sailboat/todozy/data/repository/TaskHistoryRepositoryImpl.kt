package br.com.sailboat.todozy.data.repository

import br.com.sailboat.todozy.data.DatabaseOpenHelper
import br.com.sailboat.todozy.data.mapper.mapToTaskHistoryList
import br.com.sailboat.todozy.data.model.TaskHistoryData
import br.com.sailboat.todozy.data.sqlite.TaskHistorySQLite
import br.com.sailboat.todozy.domain.filter.TaskHistoryFilter
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskHistory
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.domain.repository.TaskHistoryRepository

class TaskHistoryRepositoryImpl(database: DatabaseOpenHelper): TaskHistoryRepository {

    private val taskHistorySQLite by lazy { TaskHistorySQLite(database) }

    override suspend fun getTodayHistory(filter: TaskHistoryFilter): List<TaskHistory> {
        return taskHistorySQLite.getTodayHistory(filter).mapToTaskHistoryList()
    }

    override suspend fun getYesterdayHistory(filter: TaskHistoryFilter): List<TaskHistory> {
        return taskHistorySQLite.getYesterdayHistory(filter).mapToTaskHistoryList()
    }

    override suspend fun getPreviousDaysHistory(filter: TaskHistoryFilter): List<TaskHistory> {
        return taskHistorySQLite.getPreviousDaysHistory(filter).mapToTaskHistoryList()
    }

    override suspend fun insert(task: Task, status: TaskStatus) {
        taskHistorySQLite.save(task.id, status.id)
    }

    override suspend fun getTotalOfNotDoneTasks(filter: TaskHistoryFilter): Int {
        return taskHistorySQLite.getTotalOfNotDoneTasks(filter)
    }

    override suspend fun getTotalOfDoneTasks(filter: TaskHistoryFilter): Int {
        return taskHistorySQLite.getTotalOfDoneTasks(filter)
    }

    override suspend fun getTaskHistory(taskId: Long): List<TaskHistory> {
        return taskHistorySQLite.getTaskHistoryByTask(taskId).mapToTaskHistoryList()
    }

}