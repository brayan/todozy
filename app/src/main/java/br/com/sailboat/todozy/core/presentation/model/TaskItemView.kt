package br.com.sailboat.todozy.core.presentation.model

import br.com.sailboat.todozy.features.tasks.domain.model.Task
import java.util.*

data class TaskItemView(
    var taskId: Long,
    var taskName: String,
    var alarm: Calendar? = null,
    override val viewType: Int = ViewType.TASK.ordinal,
) : ItemView

fun Task.mapToTaskItemView() = TaskItemView(taskId = id, taskName = name, alarm = alarm?.dateTime)

fun List<Task>.mapToTaskItemView() = map { task -> task.mapToTaskItemView() }