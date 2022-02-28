package br.com.sailboat.todozy.features.tasks.domain.usecase.history

import br.com.sailboat.todozy.domain.model.TaskHistory
import br.com.sailboat.todozy.domain.model.TaskHistoryFilter

interface GetTaskHistoryUseCase {
    suspend operator fun invoke(filter: TaskHistoryFilter): List<TaskHistory>
}