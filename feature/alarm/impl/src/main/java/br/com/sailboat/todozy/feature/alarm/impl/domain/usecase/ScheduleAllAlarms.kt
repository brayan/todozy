package br.com.sailboat.todozy.feature.alarm.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.domain.model.TaskFilter
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetNextAlarmUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.ScheduleAllAlarmsUseCase
import br.com.sailboat.todozy.feature.task.list.domain.usecase.GetTasksUseCase
import br.com.sailboat.todozy.utility.kotlin.extension.isAfterNow
import br.com.sailboat.todozy.utility.kotlin.extension.isBeforeNow

class ScheduleAllAlarms(
    private val getTasksUseCase: GetTasksUseCase,
    private val getNextAlarmUseCase: GetNextAlarmUseCase,
    private val scheduleAlarmUseCase: ScheduleAlarmUseCase,
) : ScheduleAllAlarmsUseCase {

    override suspend operator fun invoke() {
        val taskFilter = TaskFilter(TaskCategory.WITH_ALARMS)
        val tasksWithAlarms = getTasksUseCase(taskFilter).getOrDefault(emptyList())

        tasksWithAlarms.forEach { task ->
            task.alarm?.let {
                var alarm = it

                if (alarm.dateTime.isBeforeNow() && alarm.isAlarmRepeating()) {
                    alarm = getNextAlarmUseCase(alarm)
                }

                if (alarm.dateTime.isAfterNow()) {
                    scheduleAlarmUseCase(alarm, task.id)
                }
            }
        }
    }
}
