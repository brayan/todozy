package br.com.sailboat.todozy.data.repository

import android.content.Context
import br.com.sailboat.todozy.data.DatabaseOpenHelper
import br.com.sailboat.todozy.data.mapper.mapToTaskHistoryList
import br.com.sailboat.todozy.data.sqlite.TaskHistorySQLite
import br.com.sailboat.todozy.domain.filter.TaskHistoryFilter
import br.com.sailboat.todozy.domain.model.TaskHistory
import br.com.sailboat.todozy.domain.repository.TaskHistoryRepository

class TaskHistoryRepositoryImpl(database: DatabaseOpenHelper): TaskHistoryRepository {

    private val taskHistorySQLite by lazy { TaskHistorySQLite(database) }

    override suspend fun getTotalOfNotDoneTasks(filter: TaskHistoryFilter): Int {
        return taskHistorySQLite.getTotalOfNotDoneTasks(filter)
    }

    override suspend fun getTotalOfDoneTasks(filter: TaskHistoryFilter): Int {
        return taskHistorySQLite.getTotalOfDoneTasks(filter)
    }

    override suspend fun getTaskHistory(filter: TaskHistoryFilter): List<TaskHistory> {
        return taskHistorySQLite.getTaskHistoryByTask(filter.taskId).mapToTaskHistoryList()
    }

}