package br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.uicomponent.impl.helper.Event
import br.com.sailboat.uicomponent.model.UiModel

internal class TaskListViewState {

    val action = Event<Action>()
    val loading = MutableLiveData(true)
    val itemsView = MutableLiveData<MutableList<UiModel>>()
    val taskMetrics = MutableLiveData<TaskMetrics>()

    sealed class Action {
        object CloseNotifications : Action()
        object NavigateToAbout : Action()
        object NavigateToHistory : Action()
        object NavigateToSettings : Action()
        object NavigateToTaskForm : Action()
        object ShowErrorLoadingTasks : Action()
        object ShowErrorCompletingTask : Action()
        data class NavigateToTaskDetails(val taskId: Long) : Action()
        data class UpdateRemovedTask(val position: Int) : Action()
    }
}
