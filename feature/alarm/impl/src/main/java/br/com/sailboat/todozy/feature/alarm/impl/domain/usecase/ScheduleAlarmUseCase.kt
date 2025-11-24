package br.com.sailboat.todozy.feature.alarm.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Alarm

internal interface ScheduleAlarmUseCase {
    suspend operator fun invoke(
        alarm: Alarm,
        taskId: Long,
    )
}
