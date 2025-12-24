package br.com.sailboat.todozy.feature.task.list.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.feature.task.history.domain.usecase.AddHistoryUseCase

internal class MarkTaskDoneForTodayUseCaseImpl(
    private val getTaskUseCase: GetTaskUseCase,
    private val addHistoryUseCase: AddHistoryUseCase,
) : MarkTaskDoneForTodayUseCase {
    override suspend operator fun invoke(taskId: Long): Result<Unit> {
        val task = getTaskUseCase(taskId).getOrElse { throwable ->
            return Result.failure(throwable)
        }

        addHistoryUseCase(task, TaskStatus.DONE).getOrElse { throwable ->
            return Result.failure(throwable)
        }

        return Result.success(Unit)
    }
}
