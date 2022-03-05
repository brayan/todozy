package br.com.sailboat.todozy.feature.task.details.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.repository.TaskRepository
import br.com.sailboat.todozy.domain.usecase.DeleteAlarmUseCase
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.DisableTaskUseCase

class DisableTask(
    private val taskRepository: TaskRepository,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
) : DisableTaskUseCase {

    override suspend operator fun invoke(task: Task) {
        taskRepository.disableTask(task)
        deleteAlarmUseCase(task.id)
    }

}