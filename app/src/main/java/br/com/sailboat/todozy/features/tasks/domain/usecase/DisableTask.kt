package br.com.sailboat.todozy.features.tasks.domain.usecase

import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskRepository
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.DeleteAlarmUseCase

class DisableTask(
    private val taskRepository: TaskRepository,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
) : DisableTaskUseCase {

    override suspend operator fun invoke(task: Task) {
        taskRepository.disableTask(task)
        deleteAlarmUseCase(task.id)
    }

}