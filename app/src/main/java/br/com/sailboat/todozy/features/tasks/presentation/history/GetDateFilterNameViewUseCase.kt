package br.com.sailboat.todozy.features.tasks.presentation.history

import br.com.sailboat.todozy.core.presentation.dialog.selectable.DateFilterTaskHistorySelectableItem

// TODO: Add unit tests
// TODO: Repalce this use case by a service
interface GetDateFilterNameViewUseCase {
    operator fun invoke(dateRangeType: DateFilterTaskHistorySelectableItem): String
}