package br.com.sailboat.todozy.features.tasks.domain.usecase.tasks

import br.com.sailboat.todozy.core.exceptions.EntityNotFoundException
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskRepository

class GetTask(private val taskRepository: TaskRepository) {

    suspend operator fun invoke(taskId: Long): Task {
        return taskRepository.getTask(taskId) ?: throw EntityNotFoundException()
    }

}