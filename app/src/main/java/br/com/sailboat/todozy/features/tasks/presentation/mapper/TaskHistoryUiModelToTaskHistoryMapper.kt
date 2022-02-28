package br.com.sailboat.todozy.features.tasks.presentation.mapper

import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskStatus
import br.com.sailboat.todozy.uicomponent.model.TaskHistoryUiModel
import br.com.sailboat.todozy.utility.kotlin.model.Entity

class TaskHistoryUiModelToTaskHistoryMapper {

    fun map(taskHistory: TaskHistoryUiModel): TaskHistory {
        val status = if (taskHistory.done) TaskStatus.DONE else TaskStatus.NOT_DONE
        return TaskHistory(
            id = taskHistory.id,
            taskId = Entity.NO_ID,
            taskName = taskHistory.taskName,
            status = status,
            insertingDate = taskHistory.insertingDate
        )
    }

}