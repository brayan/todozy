package br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel

import br.com.sailboat.uicomponent.impl.dialog.selectable.model.DateFilterTaskHistorySelectableItem
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.TaskStatusSelectableItem
import java.util.Calendar

internal sealed class TaskHistoryViewAction {
    object ScrollToTop : TaskHistoryViewAction()
    object NavigateToClearAllHistoryConfirmation : TaskHistoryViewAction()
    object ShowGenericError : TaskHistoryViewAction()
    data class NavigateToMenuFilter(
        val dateRangeType: DateFilterTaskHistorySelectableItem,
        val status: TaskStatusSelectableItem,
    ) : TaskHistoryViewAction()
    data class NavigateToDateFilter(
        val dateFilterType: DateFilterTaskHistorySelectableItem,
    ) : TaskHistoryViewAction()
    data class NavigateToDateRangeFilter(
        val initialDate: Calendar,
        val finalDate: Calendar,
    ) : TaskHistoryViewAction()
    data class NavigateToStatusFilter(val status: TaskStatusSelectableItem) : TaskHistoryViewAction()
    data class NavigateToDeleteTaskHistoryConfirmation(val position: Int) : TaskHistoryViewAction()
    data class RefreshHistoryItem(val position: Int) : TaskHistoryViewAction()
    data class ScrollToPosition(val position: Int) : TaskHistoryViewAction()
}
