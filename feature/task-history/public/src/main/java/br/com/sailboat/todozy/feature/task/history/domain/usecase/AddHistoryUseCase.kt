package br.com.sailboat.todozy.feature.task.history.domain.usecase

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskStatus

interface AddHistoryUseCase {
    suspend operator fun invoke(
        task: Task,
        status: TaskStatus,
    ): Result<Unit?>
}
