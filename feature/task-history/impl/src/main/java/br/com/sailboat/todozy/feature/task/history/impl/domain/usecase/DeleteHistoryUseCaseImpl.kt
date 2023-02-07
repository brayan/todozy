package br.com.sailboat.todozy.feature.task.history.impl.domain.usecase

import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistory
import br.com.sailboat.todozy.feature.task.history.domain.repository.TaskHistoryRepository

internal class DeleteHistoryUseCaseImpl(
    private val taskHistoryRepository: TaskHistoryRepository,
) : DeleteHistoryUseCase {

    override suspend operator fun invoke(taskHistory: TaskHistory): Result<Unit?> {
        return taskHistoryRepository.delete(taskHistory)
    }
}
