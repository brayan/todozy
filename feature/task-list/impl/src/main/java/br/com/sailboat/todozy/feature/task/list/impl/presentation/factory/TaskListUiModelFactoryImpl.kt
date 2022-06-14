package br.com.sailboat.todozy.feature.task.list.impl.presentation.factory

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.feature.task.list.impl.presentation.mapper.TaskToTaskUiModelMapper
import br.com.sailboat.todozy.feature.task.list.impl.presentation.mapper.TaskCategoryToStringMapper
import br.com.sailboat.uicomponent.model.SubheadUiModel
import br.com.sailboat.uicomponent.model.UiModel

class TaskListUiModelFactoryImpl(
    private val taskToTaskUiModelMapper: TaskToTaskUiModelMapper,
    private val taskCategoryToStringMapper: TaskCategoryToStringMapper,
) : TaskListUiModelFactory {

    override suspend fun create(tasks: List<Task>, category: TaskCategory): List<UiModel> {
        val tasksView = mutableListOf<UiModel>()
        val subhead = taskCategoryToStringMapper.map(category)

        if (tasks.isNotEmpty()) {
            tasksView.add(SubheadUiModel(subhead))
            tasksView.addAll(taskToTaskUiModelMapper.map(tasks))
        }

        return tasksView
    }
}
