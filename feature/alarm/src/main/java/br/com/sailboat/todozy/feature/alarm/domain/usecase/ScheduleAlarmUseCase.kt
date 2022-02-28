package br.com.sailboat.todozy.feature.alarm.domain.usecase

import br.com.sailboat.todozy.feature.alarm.domain.model.Alarm

interface ScheduleAlarmUseCase {
    suspend operator fun invoke(alarm: Alarm, taskId: Long)
}