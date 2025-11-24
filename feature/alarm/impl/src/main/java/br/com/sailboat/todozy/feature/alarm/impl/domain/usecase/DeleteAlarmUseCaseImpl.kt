package br.com.sailboat.todozy.feature.alarm.impl.domain.usecase

import br.com.sailboat.todozy.feature.alarm.domain.repository.AlarmRepository
import br.com.sailboat.todozy.feature.alarm.domain.usecase.DeleteAlarmUseCase

internal class DeleteAlarmUseCaseImpl(
    private val alarmRepository: AlarmRepository,
    private val cancelAlarmScheduleUseCase: CancelAlarmScheduleUseCase,
) : DeleteAlarmUseCase {
    override suspend operator fun invoke(taskId: Long): Result<Unit?> =
        runCatching {
            alarmRepository.deleteAlarmByTask(taskId).getOrThrow()
            cancelAlarmScheduleUseCase(taskId)
        }
}
