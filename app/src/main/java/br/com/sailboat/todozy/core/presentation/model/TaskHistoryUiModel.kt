package br.com.sailboat.todozy.core.presentation.model

import br.com.sailboat.todozy.utility.kotlin.model.Entity
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskStatus
import br.com.sailboat.todozy.uicomponent.model.UiModel
import br.com.sailboat.todozy.uicomponent.model.UiModelType

data class TaskHistoryUiModel(
    val id: Long,
    val taskName: String,
    var status: TaskStatusUiModel,
    val insertingDate: String,
    override val index: Int = UiModelType.TASK_HISTORY.ordinal
) : UiModel

fun TaskHistory.mapToTaskHistoryView(): TaskHistoryUiModel {
    val status = if (status == TaskStatus.DONE) TaskStatusUiModel.DONE else TaskStatusUiModel.NOT_DONE
    return TaskHistoryUiModel(
        id = id,
        taskName = taskName,
        status = status,
        insertingDate = insertingDate
    )
}

fun TaskHistoryUiModel.mapToTaskHistory(): TaskHistory {
    val status = if (status == TaskStatusUiModel.DONE) TaskStatus.DONE else TaskStatus.NOT_DONE
    return TaskHistory(
        id = id,
        taskId = Entity.NO_ID,
        taskName = taskName,
        status = status,
        insertingDate = insertingDate
    )
}

fun List<TaskHistory>.mapToTaskHistoryView() = map { history -> history.mapToTaskHistoryView() }