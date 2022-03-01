package br.com.sailboat.todozy.domain.usecase

interface DeleteAlarmUseCase {
    suspend operator fun invoke(taskId: Long)
}