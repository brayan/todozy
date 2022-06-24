package br.com.sailboat.todozy.feature.task.history.impl.domain.usecase

import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistory

internal interface UpdateHistoryUseCase {
    suspend operator fun invoke(taskHistory: TaskHistory): Result<Unit?>
}
