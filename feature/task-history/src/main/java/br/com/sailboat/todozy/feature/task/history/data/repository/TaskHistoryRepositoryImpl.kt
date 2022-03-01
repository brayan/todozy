package br.com.sailboat.todozy.feature.task.history.data.repository

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskHistory
import br.com.sailboat.todozy.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.domain.repository.TaskHistoryRepository
import br.com.sailboat.todozy.feature.task.history.data.datasource.TaskHistoryLocalDataSource
import br.com.sailboat.todozy.feature.task.history.data.model.mapToTaskHistoryData
import br.com.sailboat.todozy.feature.task.history.data.model.mapToTaskHistoryList
import br.com.sailboat.todozy.utility.android.log.logDebug

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