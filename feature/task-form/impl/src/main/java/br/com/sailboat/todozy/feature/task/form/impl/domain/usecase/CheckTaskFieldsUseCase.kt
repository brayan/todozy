package br.com.sailboat.todozy.feature.task.form.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskFieldsConditions

interface CheckTaskFieldsUseCase {
    operator fun invoke(task: Task): List<TaskFieldsConditions>
}