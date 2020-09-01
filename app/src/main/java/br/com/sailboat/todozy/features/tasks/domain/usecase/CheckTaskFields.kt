package br.com.sailboat.todozy.features.tasks.domain.usecase

import br.com.sailboat.todozy.core.extensions.isBeforeNow
import br.com.sailboat.todozy.core.extensions.safe
import br.com.sailboat.todozy.features.tasks.domain.model.Task

class CheckTaskFields {

    enum class Condition {
        TASK_NAME_NOT_FILLED,
        ALARM_NOT_VALID
    }

    class TaskFieldsException(val conditions: List<Condition>) : Exception()

    operator fun invoke(task: Task): List<Condition> {
        val conditions = mutableListOf<Condition>()

        if (task.name.isBlank()) {
            conditions.add(Condition.TASK_NAME_NOT_FILLED)
        }

        if (task.alarm?.dateTime?.isBeforeNow().safe()) {
            conditions.add(Condition.ALARM_NOT_VALID)
        }

        return conditions
    }

}