package br.com.sailboat.todozy.feature.alarm.domain.usecase

import br.com.sailboat.todozy.domain.model.Alarm

interface GetAlarmUseCase {
    suspend operator fun invoke(taskId: Long): Result<Alarm?>
}