package br.com.sailboat.todozy.feature.task.history.impl.data.repository

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistory
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.feature.task.history.domain.repository.TaskHistoryRepository
import br.com.sailboat.todozy.feature.task.history.impl.data.datasource.TaskHistoryLocalDataSource
import br.com.sailboat.todozy.feature.task.history.impl.data.model.mapToTaskHistoryData
import br.com.sailboat.todozy.feature.task.history.impl.data.model.mapToTaskHistoryList

class TaskHistoryRepositoryImpl(
    private val taskHistoryLocalDataSource: TaskHistoryLocalDataSource
) : TaskHistoryRepository {

    override suspend fun getTotalOfNotDoneTasks(filter: TaskHistoryFilter): Result<Int> {
        return taskHistoryLocalDataSource.getTotalOfNotDoneTasks(filter)
    }

    override suspend fun getTotalOfDoneTasks(filter: TaskHistoryFilter): Result<Int> {
        return taskHistoryLocalDataSource.getTotalOfDoneTasks(filter)
    }

    override suspend fun getTodayHistory(filter: TaskHistoryFilter) = runCatching {
        val taskHistoryList = taskHistoryLocalDataSource.getTodayHistory(filter).getOrThrow()
        return@runCatching taskHistoryList.mapToTaskHistoryList()
    }

    override suspend fun getYesterdayHistory(filter: TaskHistoryFilter) = runCatching {
        val taskHistoryList = taskHistoryLocalDataSource.getYesterdayHistory(filter).getOrThrow()
        return@runCatching taskHistoryList.mapToTaskHistoryList()
    }

    override suspend fun getPreviousDaysHistory(filter: TaskHistoryFilter) = runCatching {
        val taskHistoryList = taskHistoryLocalDataSource.getPreviousDaysHistory(filter).getOrThrow()
        return@runCatching taskHistoryList.mapToTaskHistoryList()
    }

    override suspend fun getTaskHistory(taskId: Long) = runCatching {
        val taskHistoryList = taskHistoryLocalDataSource.getTaskHistoryByTask(taskId).getOrThrow()
        return@runCatching taskHistoryList.mapToTaskHistoryList()
    }

    override suspend fun insert(task: Task, status: TaskStatus) = runCatching {
        taskHistoryLocalDataSource.save(task.id, status.id)
        return@runCatching
    }

    override suspend fun update(taskHistory: TaskHistory): Result<Unit?> {
        return taskHistoryLocalDataSource.update(taskHistory.mapToTaskHistoryData())
    }

    override suspend fun delete(taskHistory: TaskHistory): Result<Unit?> {
        return taskHistoryLocalDataSource.delete(taskHistory.id)
    }

    override suspend fun deleteAll(): Result<Unit?> {
        return taskHistoryLocalDataSource.deleteAllHistory()
    }
}
