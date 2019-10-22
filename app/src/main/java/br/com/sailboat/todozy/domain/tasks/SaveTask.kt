package br.com.sailboat.todozy.domain.tasks

import br.com.sailboat.todozy.domain.helper.EntityHelper
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.repository.TaskRepository


class SaveTask(private val taskRepository: TaskRepository) {

    suspend operator fun invoke(task: Task) {

        // TODO: VALIDATE

        if (task.id == EntityHelper.NO_ID) {
            taskRepository.insert(task)
        } else {
            taskRepository.update(task)
        }


    }

}