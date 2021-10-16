package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.core.extensions.isAfterNow
import br.com.sailboat.todozy.core.extensions.isBeforeNow
import br.com.sailboat.todozy.features.tasks.domain.model.TaskCategory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskFilter
import br.com.sailboat.todozy.features.tasks.domain.usecase.GetTasksUseCase

class ScheduleAllAlarms(
    private val getTasksUseCase: GetTasksUseCase,
    private val getNextAlarmUseCase: GetNextAlarmUseCase,
    private val scheduleAlarm: ScheduleAlarm,
) {

    suspend operator fun invoke() {
        val tasksWithAlarms = getTasksUseCase(TaskFilter(TaskCategory.WITH_ALARMS))

        tasksWithAlarms.forEach { task ->
            task.alarm?.let {
                var alarm = it

                if (alarm.dateTime.isBeforeNow() && alarm.isAlarmRepeating()) {
                    alarm = getNextAlarmUseCase(alarm)
                }

                if (alarm.dateTime.isAfterNow()) {
                    scheduleAlarm(alarm, task.id)
                }
            }
        }
    }

}