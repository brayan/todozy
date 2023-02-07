package br.com.sailboat.todozy.feature.task.list.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetNextAlarmUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.DisableTaskUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.feature.task.form.domain.usecase.SaveTaskUseCase
import br.com.sailboat.todozy.feature.task.history.domain.usecase.AddHistoryUseCase

internal class CompleteTaskUseCaseImpl(
    private val getTaskUseCase: GetTaskUseCase,
    private val getNextAlarmUseCase: GetNextAlarmUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val disableTaskUseCase: DisableTaskUseCase,
    private val addHistoryUseCase: AddHistoryUseCase,
) : CompleteTaskUseCase {

    override suspend operator fun invoke(taskId: Long, status: TaskStatus) {
        val task = getTaskUseCase(taskId).getOrThrow()

        task.alarm?.takeIf { it.isAlarmRepeating() }?.run {
            task.alarm = getNextAlarmUseCase(this)
            saveTaskUseCase(task).getOrThrow()
        } ?: disableTaskUseCase(task).getOrThrow()

        addHistoryUseCase(task, status).getOrThrow()
    }
}
