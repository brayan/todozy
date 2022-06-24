package br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel

import br.com.sailboat.uicomponent.impl.dialog.selectable.model.DateFilterTaskHistorySelectableItem
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.TaskStatusSelectableItem
import java.util.Calendar

internal sealed class TaskHistoryViewAction {
    object OnStart : TaskHistoryViewAction()
    object OnClickFilter : TaskHistoryViewAction()
    object OnClickDateFilter : TaskHistoryViewAction()
    object OnClickStatusFilter : TaskHistoryViewAction()
    object OnClickClearAllHistory : TaskHistoryViewAction()
    object OnClickConfirmClearAllHistory : TaskHistoryViewAction()
    data class OnSelectDateFromFilter(val date: DateFilterTaskHistorySelectableItem) :
        TaskHistoryViewAction()

    data class OnSelectStatusFromFilter(val status: TaskStatusSelectableItem) :
        TaskHistoryViewAction()

    data class OnClickDeleteTaskHistoryItem(val position: Int) : TaskHistoryViewAction()
    data class OnClickConfirmDeleteTaskHistory(val position: Int) : TaskHistoryViewAction()
    data class OnClickMarkTaskAsDone(val position: Int) : TaskHistoryViewAction()
    data class OnClickMarkTaskAsNotDone(val position: Int) : TaskHistoryViewAction()
    data class OnClickTaskHistory(val position: Int) : TaskHistoryViewAction()
    data class OnSelectDateRange(val initialDate: Calendar, val finalDate: Calendar) :
        TaskHistoryViewAction()

    data class OnSubmitSearchTerm(val searchTerm: String) : TaskHistoryViewAction()
}
