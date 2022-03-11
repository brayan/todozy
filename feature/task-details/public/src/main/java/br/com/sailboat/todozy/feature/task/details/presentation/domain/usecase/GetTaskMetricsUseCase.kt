package br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.domain.model.TaskMetrics

interface GetTaskMetricsUseCase {
    suspend operator fun invoke(filter: TaskHistoryFilter): Result<TaskMetrics>
}