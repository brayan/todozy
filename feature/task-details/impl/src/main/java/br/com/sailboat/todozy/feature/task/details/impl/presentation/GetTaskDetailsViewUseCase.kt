package br.com.sailboat.todozy.feature.task.details.impl.presentation

import br.com.sailboat.todozy.uicomponent.model.UiModel

interface GetTaskDetailsViewUseCase {
    suspend operator fun invoke(taskId: Long): Result<List<UiModel>>
}