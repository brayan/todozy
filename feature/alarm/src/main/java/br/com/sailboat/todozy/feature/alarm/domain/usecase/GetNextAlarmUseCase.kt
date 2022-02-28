package br.com.sailboat.todozy.feature.alarm.domain.usecase

import br.com.sailboat.todozy.feature.alarm.domain.model.Alarm

interface GetNextAlarmUseCase {
    operator fun invoke(alarm: Alarm): Alarm
}