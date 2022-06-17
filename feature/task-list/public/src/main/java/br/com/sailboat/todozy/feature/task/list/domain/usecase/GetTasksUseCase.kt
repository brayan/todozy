package br.com.sailboat.todozy.feature.task.list.domain.usecase

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskFilter

interface GetTasksUseCase {
    suspend operator fun invoke(filter: TaskFilter): Result<List<Task>>
}
