package br.com.sailboat.todozy.feature.task.list.impl.domain.usecase

internal interface MarkTaskDoneForTodayUseCase {
    suspend operator fun invoke(taskId: Long): Result<Unit>
}
