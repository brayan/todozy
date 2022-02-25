package br.com.sailboat.todozy.features.tasks.presentation.list

import br.com.sailboat.todozy.uicomponent.model.UiModel

interface GetTasksViewUseCase {
    suspend operator fun invoke(search: String): List<UiModel>
}