package br.com.sailboat.todozy.features.tasks.domain.usecase.history

import br.com.sailboat.todozy.domain.model.TaskHistory
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskHistoryRepository

class DeleteHistory(
    private val taskHistoryRepository: TaskHistoryRepository,
) : DeleteHistoryUseCase {

    override suspend operator fun invoke(taskHistory: TaskHistory) {
        taskHistoryRepository.delete(taskHistory)
    }

}