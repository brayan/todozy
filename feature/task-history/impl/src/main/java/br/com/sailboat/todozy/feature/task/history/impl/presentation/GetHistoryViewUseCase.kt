package br.com.sailboat.todozy.feature.task.history.impl.presentation

import br.com.sailboat.todozy.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.uicomponent.model.UiModel

interface GetHistoryViewUseCase {
    suspend operator fun invoke(filter: TaskHistoryFilter): List<UiModel>
}