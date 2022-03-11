package br.com.sailboat.todozy.feature.task.history.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskHistory
import br.com.sailboat.todozy.feature.task.history.domain.repository.TaskHistoryRepository

class DeleteHistory(
    private val taskHistoryRepository: TaskHistoryRepository,
) : DeleteHistoryUseCase {

    override suspend operator fun invoke(taskHistory: TaskHistory): Result<Unit?> {
        return taskHistoryRepository.delete(taskHistory)
    }

}