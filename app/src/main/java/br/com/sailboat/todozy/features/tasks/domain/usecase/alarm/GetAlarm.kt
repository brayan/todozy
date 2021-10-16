package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository

class GetAlarm(
    private val alarmRepository: AlarmRepository,
) : GetAlarmUseCase {

    override suspend operator fun invoke(taskId: Long): Alarm? {
        return alarmRepository.getAlarmByTaskId(taskId)
    }

}