package br.com.sailboat.todozy.core.presentation.model

import br.com.sailboat.todozy.core.base.Entity
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskStatus
import br.com.sailboat.todozy.uicomponent.model.UiModel
import br.com.sailboat.todozy.uicomponent.model.ViewType

data class TaskHistoryView(
    val id: Long,
    val taskName: String,
    var status: TaskStatusView,
    val insertingDate: String,
    override val index: Int = ViewType.TASK_HISTORY.ordinal
) : UiModel

fun TaskHistory.mapToTaskHistoryView(): TaskHistoryView {
    val status = if (status == TaskStatus.DONE) TaskStatusView.DONE else TaskStatusView.NOT_DONE
    return TaskHistoryView(
        id = id,
        taskName = taskName,
        status = status,
        insertingDate = insertingDate
    )
}

fun TaskHistoryView.mapToTaskHistory(): TaskHistory {
    val status = if (status == TaskStatusView.DONE) TaskStatus.DONE else TaskStatus.NOT_DONE
    return TaskHistory(
        id = id,
        taskId = Entity.NO_ID,
        taskName = taskName,
        status = status,
        insertingDate = insertingDate
    )
}

fun List<TaskHistory>.mapToTaskHistoryView() = map { history -> history.mapToTaskHistoryView() }