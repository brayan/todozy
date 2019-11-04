package br.com.sailboat.todozy.domain.tasks

import br.com.sailboat.todozy.domain.helper.isBeforeNow
import br.com.sailboat.todozy.domain.model.Task

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

        if (task.alarm?.dateTime?.isBeforeNow() == true) {
            conditions.add(Condition.ALARM_NOT_VALID)
        }

        return conditions
    }

}