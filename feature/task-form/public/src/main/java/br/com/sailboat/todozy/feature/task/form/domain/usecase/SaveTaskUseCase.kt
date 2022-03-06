package br.com.sailboat.todozy.feature.task.form.domain.usecase

import br.com.sailboat.todozy.domain.model.Task

interface SaveTaskUseCase {
    suspend operator fun invoke(task: Task)
}