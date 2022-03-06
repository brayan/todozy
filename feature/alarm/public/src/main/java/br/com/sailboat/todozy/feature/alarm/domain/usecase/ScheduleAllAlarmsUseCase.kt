package br.com.sailboat.todozy.feature.alarm.domain.usecase

interface ScheduleAllAlarmsUseCase {
    suspend operator fun invoke()
}