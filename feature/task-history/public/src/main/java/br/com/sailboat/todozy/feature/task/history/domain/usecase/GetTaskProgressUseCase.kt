package br.com.sailboat.todozy.feature.task.history.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskProgressDay
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskProgressFilter

interface GetTaskProgressUseCase {
    suspend operator fun invoke(filter: TaskProgressFilter): Result<List<TaskProgressDay>>
}
