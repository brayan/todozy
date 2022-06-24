package br.com.sailboat.todozy.feature.task.history.impl.domain.usecase

internal interface DeleteAllHistoryUseCase {
    suspend operator fun invoke(): Result<Unit?>
}
