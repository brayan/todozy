package br.com.sailboat.todozy.feature.task.details.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.repository.TaskRepository
import br.com.sailboat.todozy.feature.alarm.domain.usecase.DeleteAlarmUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.DisableTaskUseCase

internal class DisableTaskUseCaseImpl(
    private val taskRepository: TaskRepository,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
) : DisableTaskUseCase {
    override suspend operator fun invoke(task: Task): Result<Task> =
        runCatching {
            taskRepository.disableTask(task).getOrThrow()
            deleteAlarmUseCase(task.id)

            return@runCatching task
        }
}
