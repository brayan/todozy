package br.com.sailboat.todozy.feature.task.history.impl.domain.usecase

import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistory
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter

interface GetTaskHistoryUseCase {
    suspend operator fun invoke(filter: TaskHistoryFilter): Result<List<TaskHistory>>
}
