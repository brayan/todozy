package br.com.sailboat.todozy.features.tasks.presentation.details

import br.com.sailboat.todozy.uicomponent.model.UiModel

interface GetTaskDetailsViewUseCase {
    suspend operator fun invoke(taskId: Long): List<UiModel>
}