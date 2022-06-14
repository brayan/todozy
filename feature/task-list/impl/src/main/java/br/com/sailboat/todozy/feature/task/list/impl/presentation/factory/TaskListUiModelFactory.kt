package br.com.sailboat.todozy.feature.task.list.impl.presentation.factory

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.uicomponent.model.UiModel

interface TaskListUiModelFactory {
    suspend fun create(tasks: List<Task>, category: TaskCategory): List<UiModel>
}
