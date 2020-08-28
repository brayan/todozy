package br.com.sailboat.todozy.features.tasks.data.model

import br.com.sailboat.todozy.core.base.Entity
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskStatus

data class TaskHistoryData(var id: Long = Entity.NO_ID,
                           var taskId: Long = 0,
                           var taskName: String?,
                           var status: Int = 0,
                           var insertingDate: String?,
                           var enabled: Boolean = true)

fun TaskHistoryData.mapToTaskHistory() = TaskHistory(
        id = id,
        taskId = taskId,
        taskName = taskName ?: "",
        status = TaskStatus.getById(status),
        insertingDate = insertingDate ?: "")

fun List<TaskHistoryData>.mapToTaskHistoryList() = map { it.mapToTaskHistory() }

fun TaskHistory.mapToTaskHistoryData() = TaskHistoryData(id = id,
        taskId = taskId,
        taskName = taskName,
        status = status.id,
        insertingDate = insertingDate,
        enabled = true)