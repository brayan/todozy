package br.com.sailboat.todozy.features.tasks.domain.usecase

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskFieldsConditions

interface CheckTaskFieldsUseCase {
    operator fun invoke(task: Task): List<TaskFieldsConditions>
}