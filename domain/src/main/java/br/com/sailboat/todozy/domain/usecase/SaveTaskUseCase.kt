package br.com.sailboat.todozy.domain.usecase

import br.com.sailboat.todozy.domain.model.Task

interface SaveTaskUseCase {
    suspend operator fun invoke(task: Task)
}