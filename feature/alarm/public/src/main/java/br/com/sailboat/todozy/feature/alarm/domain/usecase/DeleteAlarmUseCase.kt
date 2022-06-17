package br.com.sailboat.todozy.feature.alarm.domain.usecase

interface DeleteAlarmUseCase {
    suspend operator fun invoke(taskId: Long): Result<Unit?>
}
