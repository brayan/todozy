package br.com.sailboat.todozy.features.tasks.domain.usecase.history

import br.com.sailboat.todozy.domain.model.TaskHistory

interface DeleteHistoryUseCase {
    suspend operator fun invoke(taskHistory: TaskHistory)
}