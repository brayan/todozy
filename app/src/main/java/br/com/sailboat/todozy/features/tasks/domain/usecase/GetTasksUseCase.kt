package br.com.sailboat.todozy.features.tasks.domain.usecase

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskFilter

interface GetTasksUseCase {
    suspend operator fun invoke(filter: TaskFilter): List<Task>
}