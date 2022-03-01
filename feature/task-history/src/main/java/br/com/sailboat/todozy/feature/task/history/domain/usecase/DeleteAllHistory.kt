package br.com.sailboat.todozy.feature.task.history.domain.usecase

import br.com.sailboat.todozy.domain.repository.TaskHistoryRepository

class DeleteAllHistory(
    private val taskHistoryRepository: TaskHistoryRepository,
) : DeleteAllHistoryUseCase {

    override suspend operator fun invoke() {
        taskHistoryRepository.deleteAll()
    }

}