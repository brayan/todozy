package br.com.sailboat.todozy.features.tasks.data.repository

import br.com.sailboat.todozy.core.platform.DatabaseOpenHelper
import br.com.sailboat.todozy.features.tasks.data.datasource.local.TaskHistoryLocalDataSourceSQLite
import br.com.sailboat.todozy.features.tasks.data.model.mapToTaskHistoryData
import br.com.sailboat.todozy.features.tasks.data.model.mapToTaskHistoryList
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.features.tasks.domain.model.TaskStatus
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskHistoryRepository

class TaskHistoryRepositoryImpl(database: DatabaseOpenHelper) : TaskHistoryRepository {

    private val taskHistorySQLite by lazy { TaskHistoryLocalDataSourceSQLite(database) }

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

    override suspend fun update(taskHistory: TaskHistory) {
        taskHistorySQLite.update(taskHistory.mapToTaskHistoryData())
    }

    override suspend fun delete(taskHistory: TaskHistory) {
        taskHistorySQLite.delete(taskHistory.id)
    }

    override suspend fun deleteAll() {
        taskHistorySQLite.deleteAllHistory()
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