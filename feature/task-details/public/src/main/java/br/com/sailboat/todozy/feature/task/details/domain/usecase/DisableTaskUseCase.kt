package br.com.sailboat.todozy.feature.task.details.domain.usecase

import br.com.sailboat.todozy.domain.model.Task

interface DisableTaskUseCase {
    suspend operator fun invoke(task: Task): Result<Task>
}