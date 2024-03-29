package br.com.sailboat.todozy.feature.task.history.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.history.domain.repository.TaskHistoryRepository
import br.com.sailboat.todozy.feature.task.history.domain.usecase.AddHistoryUseCase

internal class AddHistoryUseCaseImpl(
    private val taskHistoryRepository: TaskHistoryRepository,
) : AddHistoryUseCase {

    override suspend operator fun invoke(task: Task, status: TaskStatus): Result<Unit?> {
        return taskHistoryRepository.insert(task, status)
    }
}
