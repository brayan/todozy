package br.com.sailboat.todozy.domain.usecase

import br.com.sailboat.todozy.domain.model.Task

interface DisableTaskUseCase {
    suspend operator fun invoke(task: Task)
}