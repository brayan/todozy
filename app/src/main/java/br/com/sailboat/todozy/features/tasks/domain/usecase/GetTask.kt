package br.com.sailboat.todozy.features.tasks.domain.usecase

import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskRepository

class GetTask(private val taskRepository: TaskRepository) : GetTaskUseCase {

    override suspend operator fun invoke(taskId: Long): Task {
        return taskRepository.getTask(taskId)
    }

}