package br.com.sailboat.todozy.feature.alarm.domain.usecase

import br.com.sailboat.todozy.domain.model.Alarm

interface SaveAlarmUseCase {
    suspend operator fun invoke(alarm: Alarm, taskId: Long): Result<Unit?>
}
