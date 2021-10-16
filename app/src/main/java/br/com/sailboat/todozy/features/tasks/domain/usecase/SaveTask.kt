package br.com.sailboat.todozy.features.tasks.domain.usecase

import br.com.sailboat.todozy.core.base.Entity
import br.com.sailboat.todozy.features.tasks.domain.TaskFieldsException
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskRepository
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.DeleteAlarmUseCase
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.SaveAlarm

class SaveTask(
    private val taskRepository: TaskRepository,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
    private val saveAlarm: SaveAlarm,
    private val checkTaskFieldsUseCase: CheckTaskFieldsUseCase,
) : SaveTaskUseCase {

    override suspend operator fun invoke(task: Task) {
        val conditions = checkTaskFieldsUseCase(task)

        if (conditions.isNotEmpty()) {
            throw TaskFieldsException(conditions)
        }

        if (task.id == Entity.NO_ID) {
            taskRepository.insert(task)
        } else {
            taskRepository.update(task)
            deleteAlarmUseCase(task.id)
        }

        task.alarm?.run { saveAlarm(this, task.id) }
    }

}