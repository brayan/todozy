package br.com.sailboat.todozy.feature.task.list.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.alarm.domain.usecase.AddHistoryUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetNextAlarmUseCase
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.DisableTaskUseCase
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.feature.task.form.domain.usecase.SaveTaskUseCase

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