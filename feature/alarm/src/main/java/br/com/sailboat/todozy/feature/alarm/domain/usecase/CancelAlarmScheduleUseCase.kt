package br.com.sailboat.todozy.feature.alarm.domain.usecase

interface CancelAlarmScheduleUseCase {
    suspend operator fun invoke(taskId: Long)
}