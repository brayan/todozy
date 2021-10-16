package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.model.Alarm

interface SaveAlarmUseCase {
    suspend operator fun invoke(alarm: Alarm, taskId: Long)
}