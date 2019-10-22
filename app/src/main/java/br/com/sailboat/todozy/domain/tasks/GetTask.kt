package br.com.sailboat.todozy.domain.tasks

import br.com.sailboat.todozy.domain.exceptions.EntityNotFoundException
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.repository.TaskRepository

class GetTask(private val taskRepository: TaskRepository) {

    suspend operator fun invoke(taskId: Long): Task {
        return taskRepository.getTask(taskId) ?: throw EntityNotFoundException()
    }

}