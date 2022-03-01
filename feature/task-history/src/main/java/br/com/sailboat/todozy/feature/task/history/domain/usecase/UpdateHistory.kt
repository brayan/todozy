package br.com.sailboat.todozy.feature.task.history.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskHistory
import br.com.sailboat.todozy.feature.task.history.domain.repository.TaskHistoryRepository

class UpdateHistory(
    private val taskHistoryRepository: TaskHistoryRepository,
) : UpdateHistoryUseCase {

    override suspend operator fun invoke(taskHistory: TaskHistory) {
        taskHistoryRepository.update(taskHistory)
    }

}