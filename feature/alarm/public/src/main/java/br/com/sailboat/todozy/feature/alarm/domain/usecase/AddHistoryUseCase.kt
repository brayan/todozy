package br.com.sailboat.todozy.feature.alarm.domain.usecase

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskStatus

interface AddHistoryUseCase {
    suspend operator fun invoke(task: Task, status: TaskStatus)
}