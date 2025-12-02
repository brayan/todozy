package br.com.sailboat.todozy.feature.task.list.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskStatus

internal interface CompleteTaskUseCase {
    suspend operator fun invoke(
        taskId: Long,
        status: TaskStatus,
    ): Result<Unit>
}
