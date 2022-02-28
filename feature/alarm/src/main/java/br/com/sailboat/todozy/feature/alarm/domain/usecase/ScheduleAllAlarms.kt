package br.com.sailboat.todozy.feature.alarm.domain.usecase

import br.com.sailboat.todozy.utility.kotlin.extension.isAfterNow
import br.com.sailboat.todozy.utility.kotlin.extension.isBeforeNow

class ScheduleAllAlarms(
//    private val getTasksUseCase: GetTasksUseCase,
    private val getNextAlarmUseCase: GetNextAlarmUseCase,
    private val scheduleAlarmUseCase: ScheduleAlarmUseCase,
) : ScheduleAllAlarmsUseCase {

    override suspend operator fun invoke() {
//        val tasksWithAlarms = getTasksUseCase(TaskFilter(TaskCategory.WITH_ALARMS))
//
//        tasksWithAlarms.forEach { task ->
//            task.alarm?.let {
//                var alarm = it
//
//                if (alarm.dateTime.isBeforeNow() && alarm.isAlarmRepeating()) {
//                    alarm = getNextAlarmUseCase(alarm)
//                }
//
//                if (alarm.dateTime.isAfterNow()) {
//                    scheduleAlarmUseCase(alarm, task.id)
//                }
//            }
//        }
    }

}