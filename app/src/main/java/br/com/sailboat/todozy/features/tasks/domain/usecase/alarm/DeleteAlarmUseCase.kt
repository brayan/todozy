package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

interface DeleteAlarmUseCase {
    suspend operator fun invoke(taskId: Long)
}