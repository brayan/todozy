package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

interface CancelAlarmScheduleUseCase {
    suspend operator fun invoke(taskId: Long)
}