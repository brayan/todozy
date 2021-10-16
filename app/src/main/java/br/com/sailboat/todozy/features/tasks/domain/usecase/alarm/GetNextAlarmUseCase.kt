package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.model.Alarm

interface GetNextAlarmUseCase {
    operator fun invoke(alarm: Alarm): Alarm
}