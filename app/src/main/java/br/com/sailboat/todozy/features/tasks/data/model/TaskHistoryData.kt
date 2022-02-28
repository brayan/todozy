package br.com.sailboat.todozy.features.tasks.data.model

import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskStatus
import br.com.sailboat.todozy.utility.kotlin.model.Entity

data class TaskHistoryData(
    var id: Long = Entity.NO_ID,
    var taskId: Long = 0,
    var taskName: String?,
    var status: Int = 0,
    var insertingDate: String?,
    var enabled: Boolean = true,
)

// TODO: Move mappers into a specific class
fun TaskHistoryData.mapToTaskHistory() =
    TaskHistory(
        id = id,
        taskId = taskId,
        taskName = taskName.orEmpty(),
        status = TaskStatus.getById(status),
        insertingDate = insertingDate.orEmpty(),
    )

fun List<TaskHistoryData>.mapToTaskHistoryList() = map { it.mapToTaskHistory() }

fun TaskHistory.mapToTaskHistoryData(): TaskHistoryData =
    TaskHistoryData(
        id = id,
        taskId = taskId,
        taskName = taskName,
        status = status.id,
        insertingDate = insertingDate,
        enabled = true
    )