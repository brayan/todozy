package br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel

import br.com.sailboat.uicomponent.impl.dialog.selectable.model.DateFilterTaskHistorySelectableItem
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.TaskStatusSelectableItem
import java.util.Calendar

internal sealed class TaskHistoryViewIntent {
    object OnStart : TaskHistoryViewIntent()
    object OnClickFilter : TaskHistoryViewIntent()
    object OnClickDateFilter : TaskHistoryViewIntent()
    object OnClickStatusFilter : TaskHistoryViewIntent()
    object OnClickClearAllHistory : TaskHistoryViewIntent()
    object OnClickConfirmClearAllHistory : TaskHistoryViewIntent()
    data class OnSelectDateFromFilter(
        val date: DateFilterTaskHistorySelectableItem,
    ) : TaskHistoryViewIntent()
    data class OnSelectStatusFromFilter(
        val status: TaskStatusSelectableItem,
    ) : TaskHistoryViewIntent()
    data class OnClickDeleteTaskHistoryItem(val position: Int) : TaskHistoryViewIntent()
    data class OnClickConfirmDeleteTaskHistory(val position: Int) : TaskHistoryViewIntent()
    data class OnClickMarkTaskAsDone(val position: Int) : TaskHistoryViewIntent()
    data class OnClickMarkTaskAsNotDone(val position: Int) : TaskHistoryViewIntent()
    data class OnClickTaskHistory(val position: Int) : TaskHistoryViewIntent()
    data class OnSelectDateRange(
        val initialDate: Calendar,
        val finalDate: Calendar,
    ) : TaskHistoryViewIntent()
    data class OnSubmitSearchTerm(val searchTerm: String) : TaskHistoryViewIntent()
}
