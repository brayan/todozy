package br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.DateFilterTaskHistorySelectableItem
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.TaskStatusSelectableItem
import br.com.sailboat.uicomponent.impl.helper.Event
import br.com.sailboat.uicomponent.model.UiModel
import java.util.*

class TaskHistoryViewState {

    val action = Event<Action>()
    val taskHistoryList = MutableLiveData<List<UiModel>>()
    val taskMetrics = MutableLiveData<TaskMetrics>()
    val subtitle = MutableLiveData<String>()

    sealed class Action {
        data class NavigateToMenuFilter(
            val dateRangeType: DateFilterTaskHistorySelectableItem,
            val status: TaskStatusSelectableItem,
        ) : Action()

        data class NavigateToDateFilter(
            val dateFilterType: DateFilterTaskHistorySelectableItem,
        ) : Action()

        data class NavigateToDateRangeFilter(
            val initialDate: Calendar,
            val finalDate: Calendar,
        ) : Action()

        data class NavigateToStatusFilter(val status: TaskStatusSelectableItem) : Action()
        data class NavigateToDeleteTaskHistoryConfirmation(val position: Int) : Action()
        data class RefreshHistoryItem(val position: Int) : Action()
        data class ScrollToPosition(val position: Int) : Action()
        object ScrollToTop : Action()
        object NavigateToClearAllHistoryConfirmation : Action()
        object ShowGenericError : Action()
    }
}
