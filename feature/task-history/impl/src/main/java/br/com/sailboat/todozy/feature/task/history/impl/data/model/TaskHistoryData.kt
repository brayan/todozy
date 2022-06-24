package br.com.sailboat.todozy.feature.task.history.impl.data.model

import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistory
import br.com.sailboat.todozy.utility.kotlin.model.Entity

internal data class TaskHistoryData(
    var id: Long = Entity.NO_ID,
    var taskId: Long = 0,
    var taskName: String?,
    var status: Int = 0,
    var insertingDate: String?,
    var enabled: Boolean = true,
)

// TODO: Move mappers into a specific class
internal fun TaskHistoryData.mapToTaskHistory() =
    TaskHistory(
        id = id,
        taskId = taskId,
        taskName = taskName.orEmpty(),
        status = TaskStatus.getById(status),
        insertingDate = insertingDate.orEmpty(),
    )

internal fun List<TaskHistoryData>.mapToTaskHistoryList() = map { it.mapToTaskHistory() }

internal fun TaskHistory.mapToTaskHistoryData(): TaskHistoryData =
    TaskHistoryData(
        id = id,
        taskId = taskId,
        taskName = taskName,
        status = status.id,
        insertingDate = insertingDate,
        enabled = true
    )
