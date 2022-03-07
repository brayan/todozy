package br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.uicomponent.helper.Event
import br.com.sailboat.todozy.uicomponent.model.UiModel

class TaskDetailsViewState {

    val action = Event<Action>()
    val taskMetrics = MutableLiveData<TaskMetrics>()
    val taskDetails = MutableLiveData<List<UiModel>>()

    sealed class Action {
        data class NavigateToTaskForm(val taskId: Long) : Action()
        object ConfirmDeleteTask : Action()
        object CloseTaskDetails : Action()
    }

}