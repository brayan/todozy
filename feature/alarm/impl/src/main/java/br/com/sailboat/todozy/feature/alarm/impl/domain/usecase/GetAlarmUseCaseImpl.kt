package br.com.sailboat.todozy.feature.alarm.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.feature.alarm.domain.repository.AlarmRepository
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetAlarmUseCase

internal class GetAlarmUseCaseImpl(
    private val alarmRepository: AlarmRepository,
) : GetAlarmUseCase {

    override suspend operator fun invoke(taskId: Long): Result<Alarm?> {
        return alarmRepository.getAlarmByTaskId(taskId)
    }
}
