package br.com.sailboat.todozy.features.tasks.presentation.mapper

import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.uicomponent.model.TaskUiModel

class TaskToTaskUiModelMapper {

    fun map(task: Task) =
        TaskUiModel(
            taskId = task.id,
            taskName = task.name,
            alarm = task.alarm?.dateTime,
        )

}