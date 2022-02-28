package br.com.sailboat.todozy.features.tasks.domain.usecase.history

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskStatus

interface AddHistoryUseCase {
    suspend operator fun invoke(task: Task, status: TaskStatus)
}