package br.com.sailboat.todozy.features.tasks.presentation.history

import br.com.sailboat.todozy.uicomponent.dialog.selectable.model.DateFilterTaskHistorySelectableItem

// TODO: Add unit tests
// TODO: Repalce this use case by a service
interface GetDateFilterNameViewUseCase {
    operator fun invoke(dateRangeType: DateFilterTaskHistorySelectableItem): String
}