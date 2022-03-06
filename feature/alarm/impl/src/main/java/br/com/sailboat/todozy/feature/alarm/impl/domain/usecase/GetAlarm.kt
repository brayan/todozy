package br.com.sailboat.todozy.feature.alarm.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.feature.alarm.domain.repository.AlarmRepository
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetAlarmUseCase

class GetAlarm(
    private val alarmRepository: AlarmRepository,
) : GetAlarmUseCase {

    override suspend operator fun invoke(taskId: Long): Alarm? {
        return alarmRepository.getAlarmByTaskId(taskId)
    }

}