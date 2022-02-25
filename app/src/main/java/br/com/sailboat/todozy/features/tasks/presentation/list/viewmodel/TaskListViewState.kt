package br.com.sailboat.todozy.features.tasks.presentation.list.viewmodel

import androidx.lifecycle.MutableLiveData
import br.com.sailboat.todozy.core.presentation.helper.Event
import br.com.sailboat.todozy.features.tasks.domain.model.TaskMetrics
import br.com.sailboat.todozy.uicomponent.model.UiModel

class TaskListViewState {

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
        data class NavigateToTaskDetails(val taskId: Long) : Action()
        data class UpdateRemovedTask(val position: Int) : Action()
    }

}