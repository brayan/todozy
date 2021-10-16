package br.com.sailboat.todozy.features.tasks.domain.usecase

import br.com.sailboat.todozy.features.tasks.domain.model.Task

interface SaveTaskUseCase {
    suspend operator fun invoke(task: Task)
}