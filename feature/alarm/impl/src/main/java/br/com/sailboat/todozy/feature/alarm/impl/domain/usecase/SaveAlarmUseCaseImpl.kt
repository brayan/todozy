package br.com.sailboat.todozy.feature.alarm.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.feature.alarm.domain.repository.AlarmRepository
import br.com.sailboat.todozy.feature.alarm.domain.usecase.SaveAlarmUseCase

internal class SaveAlarmUseCaseImpl(
    private val alarmRepository: AlarmRepository,
    private val cancelAlarmScheduleUseCase: CancelAlarmScheduleUseCase,
    private val scheduleAlarmUseCase: ScheduleAlarmUseCase,
) : SaveAlarmUseCase {
    override suspend operator fun invoke(
        alarm: Alarm,
        taskId: Long,
    ): Result<Unit?> = runCatching {
        alarmRepository.save(alarm, taskId).getOrThrow()
        cancelAlarmScheduleUseCase(taskId)
        scheduleAlarmUseCase(alarm, taskId)
    }
}
