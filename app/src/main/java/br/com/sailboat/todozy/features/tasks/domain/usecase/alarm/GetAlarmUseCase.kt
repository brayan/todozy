package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.model.Alarm

interface GetAlarmUseCase {
    suspend operator fun invoke(taskId: Long): Alarm?
}