package br.com.sailboat.todozy.features.tasks.domain.usecase

import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.model.TaskFieldsConditions
import br.com.sailboat.todozy.utility.kotlin.extension.isBeforeNow
import br.com.sailboat.todozy.utility.kotlin.extension.isTrue

class CheckTaskFields : CheckTaskFieldsUseCase {

    override operator fun invoke(task: Task): List<TaskFieldsConditions> {
        val conditions = mutableListOf<TaskFieldsConditions>()

        if (task.name.isBlank()) {
            conditions.add(TaskFieldsConditions.TASK_NAME_NOT_FILLED)
        }

        if (task.alarm?.dateTime?.isBeforeNow().isTrue()) {
            conditions.add(TaskFieldsConditions.ALARM_NOT_VALID)
        }

        return conditions
    }

}