package br.com.sailboat.todozy.ui.mapper

import br.com.sailboat.todozy.domain.helper.EntityHelper
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskHistory
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.ui.model.TaskHistoryView
import br.com.sailboat.todozy.ui.model.TaskItemView
import br.com.sailboat.todozy.ui.model.TaskStatusView

fun Task.mapToTaskItemView() = TaskItemView(taskId = id, taskName = name, alarm = alarm?.dateTime)

fun List<Task>.mapToTaskItemView() = map { task -> task.mapToTaskItemView() }

fun TaskHistory.mapToTaskHistoryView(): TaskHistoryView {
    val status = if (status == TaskStatus.DONE) TaskStatusView.DONE else TaskStatusView.NOT_DONE
    return TaskHistoryView(
            id = id,
            taskName = taskName,
            status = status,
            insertingDate = insertingDate)
}

fun TaskHistoryView.mapToTaskHistory(): TaskHistory {
    val status = if (status == TaskStatusView.DONE) TaskStatus.DONE else TaskStatus.NOT_DONE
    return TaskHistory(
            id = id,
            taskId = EntityHelper.NO_ID,
            taskName = taskName,
            status = status,
            insertingDate = insertingDate)
}

fun List<TaskHistory>.mapToTaskHistoryView() = map { history -> history.mapToTaskHistoryView() }