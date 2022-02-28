package br.com.sailboat.todozy.features.tasks.domain.usecase

import br.com.sailboat.todozy.domain.model.Task

interface GetTaskUseCase {
    suspend operator fun invoke(taskId: Long): Task
}