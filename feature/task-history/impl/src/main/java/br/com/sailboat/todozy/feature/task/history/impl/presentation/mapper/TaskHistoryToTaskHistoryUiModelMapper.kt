package br.com.sailboat.todozy.feature.task.history.impl.presentation.mapper

import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistory
import br.com.sailboat.uicomponent.model.TaskHistoryUiModel

internal class TaskHistoryToTaskHistoryUiModelMapper {

    fun map(taskHistoryList: List<TaskHistory>) = taskHistoryList.map { map(it) }

    private fun map(taskHistory: TaskHistory): TaskHistoryUiModel {
        val done = taskHistory.status == TaskStatus.DONE

        return TaskHistoryUiModel(
            id = taskHistory.id,
            taskName = taskHistory.taskName,
            done = done,
            insertingDate = taskHistory.insertingDate
        )
    }
}
