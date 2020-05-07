package br.com.sailboat.todozy.domain.history

import br.com.sailboat.todozy.domain.model.TaskHistory
import br.com.sailboat.todozy.domain.repository.TaskHistoryRepository

class UpdateHistory(private val taskHistoryRepository: TaskHistoryRepository) {

    suspend operator fun invoke(taskHistory: TaskHistory) {
        taskHistoryRepository.update(taskHistory)
    }

}