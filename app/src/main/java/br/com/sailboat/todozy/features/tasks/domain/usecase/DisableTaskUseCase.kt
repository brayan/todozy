package br.com.sailboat.todozy.features.tasks.domain.usecase

import br.com.sailboat.todozy.domain.model.Task

interface DisableTaskUseCase {
    suspend operator fun invoke(task: Task)
}