package br.com.sailboat.todozy.features.tasks.domain.usecase.tasks

import br.com.sailboat.todozy.core.base.Entity
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskRepository

class SaveTask(private val taskRepository: TaskRepository) {

    suspend operator fun invoke(task: Task) {
        val conditions = CheckTaskFields().invoke(task)

        if (conditions.isNotEmpty()) {
            throw CheckTaskFields.TaskFieldsException(conditions)
        }

        taskRepository.run { if (task.id == Entity.NO_ID) insert(task) else update(task) }
    }

}