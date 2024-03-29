package br.com.sailboat.todozy.feature.task.history.impl.presentation

import br.com.sailboat.uicomponent.impl.dialog.selectable.model.DateFilterTaskHistorySelectableItem

internal interface GetDateFilterNameViewUseCase {
    operator fun invoke(dateRangeType: DateFilterTaskHistorySelectableItem): String
}
