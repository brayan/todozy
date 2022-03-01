package br.com.sailboat.todozy.features.tasks.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.domain.usecase.SaveTaskUseCase
import br.com.sailboat.todozy.domain.usecase.GetNextAlarmUseCase
import br.com.sailboat.todozy.feature.task.history.domain.usecase.AddHistoryUseCase

class CompleteTask(
    private val getTaskUseCase: GetTaskUseCase,
    private val getNextAlarmUseCase: GetNextAlarmUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val disableTaskUseCase: DisableTaskUseCase,
    private val addHistoryUseCase: AddHistoryUseCase,
) : CompleteTaskUseCase {

    override suspend operator fun invoke(taskId: Long, status: TaskStatus) {
        val task = getTaskUseCase(taskId)

        task.alarm?.takeIf { it.isAlarmRepeating() }?.run {

            task.alarm = getNextAlarmUseCase(this)
            saveTaskUseCase(task)

        } ?: disableTaskUseCase(task)

        addHistoryUseCase(task, status)
    }

}