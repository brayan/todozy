package br.com.sailboat.todozy.feature.task.list.impl.presentation.factory

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.feature.task.list.impl.presentation.mapper.TaskCategoryToStringMapper
import br.com.sailboat.todozy.feature.task.list.impl.presentation.mapper.TaskToTaskUiModelMapper
import br.com.sailboat.uicomponent.model.SubheadUiModel
import br.com.sailboat.uicomponent.model.UiModel

internal class TaskListUiModelFactory(
    private val taskToTaskUiModelMapper: TaskToTaskUiModelMapper,
    private val taskCategoryToStringMapper: TaskCategoryToStringMapper,
) {

    fun create(tasks: List<Task>, category: TaskCategory): List<UiModel> {
        val taskListUiModel = mutableListOf<UiModel>()
        val subhead = taskCategoryToStringMapper.map(category)

        if (tasks.isNotEmpty()) {
            taskListUiModel.add(SubheadUiModel(subhead))
            taskListUiModel.addAll(taskToTaskUiModelMapper.map(tasks))
        }

        return taskListUiModel
    }
}
