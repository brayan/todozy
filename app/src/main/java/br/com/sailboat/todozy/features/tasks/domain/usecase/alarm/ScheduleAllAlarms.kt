package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.core.extensions.isAfterNow
import br.com.sailboat.todozy.core.extensions.isBeforeNow
import br.com.sailboat.todozy.features.tasks.domain.model.TaskCategory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskFilter
import br.com.sailboat.todozy.features.tasks.domain.usecase.GetTasks

class ScheduleAllAlarms(private val getTasks: GetTasks,
                        private val getNextAlarm: GetNextAlarm,
                        private val scheduleAlarm: ScheduleAlarm) {

    suspend operator fun invoke() {
        val tasksWithAlarms = getTasks(TaskFilter(TaskCategory.WITH_ALARMS))

        tasksWithAlarms.forEach { task ->
            task.alarm?.let {
                var alarm = it

                if (alarm.dateTime.isBeforeNow() && alarm.isAlarmRepeating()) {
                    alarm = getNextAlarm(alarm)
                }

                if (alarm.dateTime.isAfterNow()) {
                    scheduleAlarm(alarm, task.id)
                }
            }
        }
    }

}