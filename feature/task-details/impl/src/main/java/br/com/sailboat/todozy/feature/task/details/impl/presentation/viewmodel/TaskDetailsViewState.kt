package br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import br.com.sailboat.uicomponent.impl.helper.Event
import br.com.sailboat.uicomponent.model.UiModel

class TaskDetailsViewState {

    val action = Event<Action>()
    val taskMetrics = MutableLiveData<TaskMetrics>()
    val taskDetails = MutableLiveData<List<UiModel>>()
    var taskId: Long = Entity.NO_ID

    sealed class Action {
        data class NavigateToTaskForm(val taskId: Long) : Action()
        data class CloseTaskDetails(val success: Boolean) : Action()
        object ConfirmDeleteTask : Action()
        object ShowErrorLoadingTaskDetails : Action()
    }
}
