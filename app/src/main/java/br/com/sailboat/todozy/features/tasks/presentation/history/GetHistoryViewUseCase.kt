package br.com.sailboat.todozy.features.tasks.presentation.history

import br.com.sailboat.todozy.core.presentation.model.ItemView
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistoryFilter

interface GetHistoryViewUseCase {
    suspend operator fun invoke(filter: TaskHistoryFilter): List<ItemView>
}