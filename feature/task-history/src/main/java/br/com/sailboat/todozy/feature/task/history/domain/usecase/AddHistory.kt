package br.com.sailboat.todozy.feature.task.history.domain.usecase

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.domain.repository.TaskHistoryRepository
import br.com.sailboat.todozy.feature.alarm.domain.usecase.AddHistoryUseCase

class AddHistory(
    private val taskHistoryRepository: TaskHistoryRepository
) : AddHistoryUseCase {

    override suspend operator fun invoke(task: Task, status: TaskStatus) {
        taskHistoryRepository.insert(task, status)
    }

}