package br.com.sailboat.todozy.ui.mapper

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.ui.model.TaskItemView

fun Task.mapToTaskItemView() = TaskItemView(taskId = id, taskName = name, alarm = alarm?.dateTime)

fun List<Task>.mapToTaskItemView() = map { task -> task.mapToTaskItemView() }