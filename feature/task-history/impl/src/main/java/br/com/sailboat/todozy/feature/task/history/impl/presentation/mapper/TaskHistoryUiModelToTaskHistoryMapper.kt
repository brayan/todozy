package br.com.sailboat.todozy.feature.task.history.impl.presentation.mapper

import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistory
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import br.com.sailboat.uicomponent.model.TaskHistoryUiModel

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
