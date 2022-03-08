package br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.uicomponent.helper.Event
import br.com.sailboat.todozy.uicomponent.model.UiModel
import br.com.sailboat.todozy.utility.kotlin.model.Entity

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