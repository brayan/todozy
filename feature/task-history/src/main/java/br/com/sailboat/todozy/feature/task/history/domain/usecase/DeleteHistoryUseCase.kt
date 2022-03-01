package br.com.sailboat.todozy.feature.task.history.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskHistory

interface DeleteHistoryUseCase {
    suspend operator fun invoke(taskHistory: TaskHistory)
}