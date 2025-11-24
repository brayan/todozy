package br.com.sailboat.todozy.feature.task.details.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.repository.TaskRepository
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskUseCase

internal class GetTaskUseCaseImpl(private val taskRepository: TaskRepository) : GetTaskUseCase {
    override suspend operator fun invoke(taskId: Long): Result<Task> {
        return taskRepository.getTask(taskId)
    }
}
