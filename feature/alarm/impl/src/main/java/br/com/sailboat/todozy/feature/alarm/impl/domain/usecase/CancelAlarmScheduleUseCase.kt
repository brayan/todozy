package br.com.sailboat.todozy.feature.alarm.impl.domain.usecase

interface CancelAlarmScheduleUseCase {
    suspend operator fun invoke(taskId: Long)
}