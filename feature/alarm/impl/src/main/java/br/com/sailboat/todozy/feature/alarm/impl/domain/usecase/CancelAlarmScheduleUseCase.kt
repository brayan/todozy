package br.com.sailboat.todozy.feature.alarm.impl.domain.usecase

internal interface CancelAlarmScheduleUseCase {
    suspend operator fun invoke(taskId: Long)
}
