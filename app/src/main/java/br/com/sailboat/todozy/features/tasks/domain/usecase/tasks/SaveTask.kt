package br.com.sailboat.todozy.features.tasks.domain.usecase.tasks

import br.com.sailboat.todozy.core.base.Entity
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskRepository
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.DeleteAlarm
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.SaveAlarm

class SaveTask(private val taskRepository: TaskRepository,
               private val deleteAlarm: DeleteAlarm,
               private val saveAlarm: SaveAlarm) {

    suspend operator fun invoke(task: Task) {
        val conditions = CheckTaskFields().invoke(task)

        if (conditions.isNotEmpty()) {
            throw CheckTaskFields.TaskFieldsException(conditions)
        }

        if (task.id == Entity.NO_ID) {
            taskRepository.insert(task)
        } else {
            taskRepository.update(task)
            deleteAlarm(task.id)
        }

        task.alarm?.run { saveAlarm(this, task.id) }
    }

}