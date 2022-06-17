package br.com.sailboat.todozy.feature.task.details.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter

interface GetTaskMetricsUseCase {
    suspend operator fun invoke(filter: TaskHistoryFilter): Result<TaskMetrics>
}
