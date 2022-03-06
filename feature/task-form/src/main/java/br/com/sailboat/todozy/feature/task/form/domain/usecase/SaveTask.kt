package br.com.sailboat.todozy.feature.task.form.domain.usecase

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskFieldsException
import br.com.sailboat.todozy.domain.repository.TaskRepository
import br.com.sailboat.todozy.domain.usecase.SaveTaskUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.DeleteAlarmUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.SaveAlarmUseCase
import br.com.sailboat.todozy.utility.kotlin.model.Entity

class SaveTask(
    private val taskRepository: TaskRepository,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
    private val saveAlarmUseCase: SaveAlarmUseCase,
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

        task.alarm?.run { saveAlarmUseCase(this, task.id) }
    }

}