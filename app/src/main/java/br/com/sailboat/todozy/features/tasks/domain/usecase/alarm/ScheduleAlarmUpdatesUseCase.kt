package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

interface ScheduleAlarmUpdatesUseCase {
    suspend operator fun invoke()
}