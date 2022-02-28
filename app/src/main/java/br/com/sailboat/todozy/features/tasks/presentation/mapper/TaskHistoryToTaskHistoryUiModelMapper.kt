package br.com.sailboat.todozy.features.tasks.presentation.mapper

import br.com.sailboat.todozy.domain.model.TaskHistory
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.uicomponent.model.TaskHistoryUiModel

class TaskHistoryToTaskHistoryUiModelMapper {

    fun map(taskHistory: TaskHistory): TaskHistoryUiModel {
        val done = taskHistory.status == TaskStatus.DONE

        return TaskHistoryUiModel(
            id = taskHistory.id,
            taskName = taskHistory.taskName,
            done = done,
            insertingDate = taskHistory.insertingDate
        )
    }

}