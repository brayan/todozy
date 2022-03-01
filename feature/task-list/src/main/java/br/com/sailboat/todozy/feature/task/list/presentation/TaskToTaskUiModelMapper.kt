package br.com.sailboat.todozy.feature.task.list.presentation

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.uicomponent.model.TaskUiModel

class TaskToTaskUiModelMapper {

    fun map(task: Task) =
        TaskUiModel(
            taskId = task.id,
            taskName = task.name,
            alarm = task.alarm?.dateTime,
        )

}