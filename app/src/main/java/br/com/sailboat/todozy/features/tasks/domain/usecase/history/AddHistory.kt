package br.com.sailboat.todozy.features.tasks.domain.usecase.history

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskHistoryRepository

class AddHistory(
    private val taskHistoryRepository: TaskHistoryRepository
) : AddHistoryUseCase {

    override suspend operator fun invoke(task: Task, status: TaskStatus) {
        taskHistoryRepository.insert(task, status)
    }

}