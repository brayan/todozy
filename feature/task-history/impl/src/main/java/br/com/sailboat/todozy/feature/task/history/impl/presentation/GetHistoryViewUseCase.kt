package br.com.sailboat.todozy.feature.task.history.impl.presentation

import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter
import br.com.sailboat.uicomponent.model.UiModel

interface GetHistoryViewUseCase {
    suspend operator fun invoke(filter: TaskHistoryFilter): Result<List<UiModel>>
}