package br.com.sailboat.todozy.features.tasks.domain.usecase.history

import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistory

interface UpdateHistoryUseCase {
    suspend operator fun invoke(taskHistory: TaskHistory)
}