package br.com.sailboat.todozy.features.tasks.presentation.details

import br.com.sailboat.todozy.core.presentation.model.ItemView

interface GetTaskDetailsViewUseCase {
    suspend operator fun invoke(taskId: Long): List<ItemView>
}