package br.com.sailboat.todozy.domain.history

import br.com.sailboat.todozy.domain.model.TaskHistory
import br.com.sailboat.todozy.domain.repository.TaskHistoryRepository

class DeleteAllHistory(private val taskHistoryRepository: TaskHistoryRepository) {

    suspend operator fun invoke() {
        taskHistoryRepository.deleteAll()
    }

}