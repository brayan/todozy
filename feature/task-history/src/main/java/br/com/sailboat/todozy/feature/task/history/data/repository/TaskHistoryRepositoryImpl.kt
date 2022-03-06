package br.com.sailboat.todozy.feature.task.history.data.repository

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskHistory
import br.com.sailboat.todozy.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.domain.repository.TaskHistoryRepository
import br.com.sailboat.todozy.feature.task.history.data.datasource.TaskHistoryLocalDataSource
import br.com.sailboat.todozy.feature.task.history.data.model.mapToTaskHistoryData
import br.com.sailboat.todozy.feature.task.history.data.model.mapToTaskHistoryList

class TaskHistoryRepositoryImpl(
    private val taskHistoryLocalDataSource: TaskHistoryLocalDataSource
) : TaskHistoryRepository {

    override suspend fun getTotalOfNotDoneTasks(filter: TaskHistoryFilter): Int {
        return taskHistoryLocalDataSource.getTotalOfNotDoneTasks(filter)
    }

    override suspend fun getTotalOfDoneTasks(filter: TaskHistoryFilter): Int {
        return taskHistoryLocalDataSource.getTotalOfDoneTasks(filter)
    }

    override suspend fun getTodayHistory(filter: TaskHistoryFilter): List<TaskHistory> {
        return taskHistoryLocalDataSource.getTodayHistory(filter).mapToTaskHistoryList()
    }

    override suspend fun getYesterdayHistory(filter: TaskHistoryFilter): List<TaskHistory> {
        return taskHistoryLocalDataSource.getYesterdayHistory(filter).mapToTaskHistoryList()
    }

    override suspend fun getPreviousDaysHistory(filter: TaskHistoryFilter): List<TaskHistory> {
        return taskHistoryLocalDataSource.getPreviousDaysHistory(filter).mapToTaskHistoryList()
    }

    override suspend fun getTaskHistory(taskId: Long): List<TaskHistory> {
        return taskHistoryLocalDataSource.getTaskHistoryByTask(taskId).mapToTaskHistoryList()
    }

    override suspend fun insert(task: Task, status: TaskStatus) {
        taskHistoryLocalDataSource.save(task.id, status.id)
    }

    override suspend fun update(taskHistory: TaskHistory) {
        taskHistoryLocalDataSource.update(taskHistory.mapToTaskHistoryData())
    }

    override suspend fun delete(taskHistory: TaskHistory) {
        taskHistoryLocalDataSource.delete(taskHistory.id)
    }

    override suspend fun deleteAll() {
        taskHistoryLocalDataSource.deleteAllHistory()
    }

}