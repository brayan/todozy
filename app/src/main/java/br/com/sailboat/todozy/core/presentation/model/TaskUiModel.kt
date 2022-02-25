package br.com.sailboat.todozy.core.presentation.model

import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.uicomponent.model.UiModel
import br.com.sailboat.todozy.uicomponent.model.ViewType
import java.util.*

data class TaskUiModel(
    var taskId: Long,
    var taskName: String,
    var alarm: Calendar? = null,
    override val index: Int = ViewType.TASK.ordinal,
) : UiModel

fun Task.mapToTaskItemView() = TaskUiModel(taskId = id, taskName = name, alarm = alarm?.dateTime)

fun List<Task>.mapToTaskItemView() = map { task -> task.mapToTaskItemView() }