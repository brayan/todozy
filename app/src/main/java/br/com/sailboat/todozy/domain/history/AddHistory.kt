package br.com.sailboat.todozy.domain.history

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.domain.repository.TaskHistoryRepository


class AddHistory(private val taskHistoryRepository: TaskHistoryRepository) {

    suspend operator fun invoke(task: Task, status: TaskStatus) {
        taskHistoryRepository.insert(task, status)
    }

}