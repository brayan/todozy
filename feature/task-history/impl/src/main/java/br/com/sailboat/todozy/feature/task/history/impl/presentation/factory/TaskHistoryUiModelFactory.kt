package br.com.sailboat.todozy.feature.task.history.impl.presentation.factory

import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistory
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryCategory
import br.com.sailboat.todozy.feature.task.history.impl.presentation.mapper.TaskHistoryCategoryToStringMapper
import br.com.sailboat.todozy.feature.task.history.impl.presentation.mapper.TaskHistoryToTaskHistoryUiModelMapper
import br.com.sailboat.uicomponent.model.SubheadUiModel
import br.com.sailboat.uicomponent.model.UiModel

internal class TaskHistoryUiModelFactory(
    private val taskHistoryCategoryToStringMapper: TaskHistoryCategoryToStringMapper,
    private val taskHistoryToTaskHistoryUiModelMapper: TaskHistoryToTaskHistoryUiModelMapper,
) {

    fun create(taskHistoryList: List<TaskHistory>, category: TaskHistoryCategory): List<UiModel> {
        val taskHistoryUiModel = mutableListOf<UiModel>()
        val subhead = taskHistoryCategoryToStringMapper.map(category)

        if (taskHistoryList.isNotEmpty()) {
            taskHistoryUiModel.add(SubheadUiModel(subhead))
            taskHistoryUiModel.addAll(taskHistoryToTaskHistoryUiModelMapper.map(taskHistoryList))
        }

        return taskHistoryUiModel
    }
}
