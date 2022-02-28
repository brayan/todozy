package br.com.sailboat.todozy.features.tasks.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskStatus

interface CompleteTaskUseCase {
    suspend operator fun invoke(taskId: Long, status: TaskStatus)
}