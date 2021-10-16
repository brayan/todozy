package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository

class SaveAlarm(
    private val alarmRepository: AlarmRepository,
    private val cancelAlarmScheduleUseCase: CancelAlarmScheduleUseCase,
    private val scheduleAlarmUseCase: ScheduleAlarmUseCase,
) : SaveAlarmUseCase {

    override suspend operator fun invoke(alarm: Alarm, taskId: Long) {
        alarmRepository.save(alarm, taskId)
        cancelAlarmScheduleUseCase(taskId)
        scheduleAlarmUseCase(alarm, taskId)
    }

}