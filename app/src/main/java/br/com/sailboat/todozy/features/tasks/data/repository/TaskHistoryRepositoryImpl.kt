package br.com.sailboat.todozy.features.tasks.data.repository

import br.com.sailboat.todozy.core.extensions.logDebug
import br.com.sailboat.todozy.core.platform.DatabaseOpenHelper
import br.com.sailboat.todozy.features.tasks.data.datasource.local.TaskHistoryLocalDataSource
import br.com.sailboat.todozy.features.tasks.data.datasource.local.TaskHistoryLocalDataSourceSQLite
import br.com.sailboat.todozy.features.tasks.data.model.mapToTaskHistoryData
import br.com.sailboat.todozy.features.tasks.data.model.mapToTaskHistoryList
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.features.tasks.domain.model.TaskStatus
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskHistoryRepository

class TaskHistoryRepositoryImpl(
    private val taskHistoryLocalDataSource: TaskHistoryLocalDataSource
) : TaskHistoryRepository {

    override suspend fun getTotalOfNotDoneTasks(filter: TaskHistoryFilter): Int {
        "${javaClass.simpleName}.getTotalOfNotDoneTasks($filter)".logDebug()
        return taskHistoryLocalDataSource.getTotalOfNotDoneTasks(filter)
    }

    override suspend fun getTotalOfDoneTasks(filter: TaskHistoryFilter): Int {
        "${javaClass.simpleName}.getTotalOfDoneTasks($filter)".logDebug()
        return taskHistoryLocalDataSource.getTotalOfDoneTasks(filter)
    }

    override suspend fun getTodayHistory(filter: TaskHistoryFilter): List<TaskHistory> {
        "${javaClass.simpleName}.getTodayHistory($filter)".logDebug()
        return taskHistoryLocalDataSource.getTodayHistory(filter).mapToTaskHistoryList()
    }

    override suspend fun getYesterdayHistory(filter: TaskHistoryFilter): List<TaskHistory> {
        "${javaClass.simpleName}.getYesterdayHistory($filter)".logDebug()
        return taskHistoryLocalDataSource.getYesterdayHistory(filter).mapToTaskHistoryList()
    }

    override suspend fun getPreviousDaysHistory(filter: TaskHistoryFilter): List<TaskHistory> {
        "${javaClass.simpleName}.getPreviousDaysHistory($filter)".logDebug()
        return taskHistoryLocalDataSource.getPreviousDaysHistory(filter).mapToTaskHistoryList()
    }

    override suspend fun getTaskHistory(taskId: Long): List<TaskHistory> {
        "${javaClass.simpleName}.getTaskHistory($taskId)".logDebug()
        return taskHistoryLocalDataSource.getTaskHistoryByTask(taskId).mapToTaskHistoryList()
    }

    override suspend fun insert(task: Task, status: TaskStatus) {
        "${javaClass.simpleName}.insert($task, $status)".logDebug()
        taskHistoryLocalDataSource.save(task.id, status.id)
    }

    override suspend fun update(taskHistory: TaskHistory) {
        "${javaClass.simpleName}.update($taskHistory)".logDebug()
        taskHistoryLocalDataSource.update(taskHistory.mapToTaskHistoryData())
    }

    override suspend fun delete(taskHistory: TaskHistory) {
        "${javaClass.simpleName}.delete($taskHistory)".logDebug()
        taskHistoryLocalDataSource.delete(taskHistory.id)
    }

    override suspend fun deleteAll() {
        "${javaClass.simpleName}.deleteAll()".logDebug()
        taskHistoryLocalDataSource.deleteAllHistory()
    }

}