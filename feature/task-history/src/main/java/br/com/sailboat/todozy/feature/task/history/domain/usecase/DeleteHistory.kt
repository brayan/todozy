package br.com.sailboat.todozy.feature.task.history.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskHistory
import br.com.sailboat.todozy.domain.repository.TaskHistoryRepository

class DeleteHistory(
    private val taskHistoryRepository: TaskHistoryRepository,
) : DeleteHistoryUseCase {

    override suspend operator fun invoke(taskHistory: TaskHistory) {
        taskHistoryRepository.delete(taskHistory)
    }

}