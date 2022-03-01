package br.com.sailboat.todozy.feature.task.history.domain.usecase

interface DeleteAllHistoryUseCase {
    suspend operator fun invoke()
}