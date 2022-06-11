package br.com.sailboat.todozy.feature.task.list.impl.presentation

import br.com.sailboat.uicomponent.model.UiModel

interface GetTasksViewUseCase {
    suspend operator fun invoke(search: String): Result<List<UiModel>>
}