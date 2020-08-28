package br.com.sailboat.todozy.features.tasks.domain.usecase.history

import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.model.TaskStatus
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskHistoryRepository


class AddHistory(private val taskHistoryRepository: TaskHistoryRepository) {

    suspend operator fun invoke(task: Task, status: TaskStatus) {
        taskHistoryRepository.insert(task, status)
    }

}