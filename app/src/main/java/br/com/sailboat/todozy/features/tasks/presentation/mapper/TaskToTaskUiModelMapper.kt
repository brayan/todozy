package br.com.sailboat.todozy.features.tasks.presentation.mapper

import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.uicomponent.model.TaskUiModel
import br.com.sailboat.todozy.utility.kotlin.model.Mapper

class TaskToTaskUiModelMapper : Mapper<Task, TaskUiModel> {

    override fun map(from: Task) =
        TaskUiModel(
            taskId = from.id,
            taskName = from.name,
            alarm = from.alarm?.dateTime,
        )

}