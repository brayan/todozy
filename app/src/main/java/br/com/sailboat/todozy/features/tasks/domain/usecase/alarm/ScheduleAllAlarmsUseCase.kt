package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

interface ScheduleAllAlarmsUseCase {
    suspend operator fun invoke()
}