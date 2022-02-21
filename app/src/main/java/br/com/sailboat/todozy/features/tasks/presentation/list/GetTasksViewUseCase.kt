package br.com.sailboat.todozy.features.tasks.presentation.list

import br.com.sailboat.todozy.core.presentation.model.ItemView

interface GetTasksViewUseCase {
    suspend operator fun invoke(search: String): List<ItemView>
}