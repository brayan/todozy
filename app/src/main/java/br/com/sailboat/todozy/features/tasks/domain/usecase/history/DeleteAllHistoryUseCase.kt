package br.com.sailboat.todozy.features.tasks.domain.usecase.history

interface DeleteAllHistoryUseCase {
    suspend operator fun invoke()
}