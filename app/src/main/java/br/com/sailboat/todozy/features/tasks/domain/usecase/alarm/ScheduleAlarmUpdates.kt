package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository
import br.com.sailboat.todozy.utility.kotlin.extension.getInitialCalendarForTomorrow

class ScheduleAlarmUpdates(
    private val alarmRepository: AlarmRepository,
) : ScheduleAlarmUpdatesUseCase {

    override suspend operator fun invoke() {
        val calendar = getInitialCalendarForTomorrow()
        alarmRepository.scheduleAlarmUpdates(calendar)
    }

}