package br.com.sailboat.todozy.features.tasks.domain.usecase.history

import br.com.sailboat.todozy.features.tasks.domain.repository.TaskHistoryRepository

class DeleteAllHistory(
    private val taskHistoryRepository: TaskHistoryRepository,
) : DeleteAllHistoryUseCase {

    override suspend operator fun invoke() {
        taskHistoryRepository.deleteAll()
    }

}