package br.com.sailboat.todozy.features.tasks.presentation.history

import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.uicomponent.model.UiModel

interface GetHistoryViewUseCase {
    suspend operator fun invoke(filter: TaskHistoryFilter): List<UiModel>
}