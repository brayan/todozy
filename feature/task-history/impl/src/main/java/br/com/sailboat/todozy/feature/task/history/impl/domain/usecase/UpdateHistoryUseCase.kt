package br.com.sailboat.todozy.feature.task.history.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskHistory

interface UpdateHistoryUseCase {
    suspend operator fun invoke(taskHistory: TaskHistory): Result<Unit?>
}