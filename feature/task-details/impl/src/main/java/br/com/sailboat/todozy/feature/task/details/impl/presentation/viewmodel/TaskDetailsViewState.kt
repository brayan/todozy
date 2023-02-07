package br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.utility.android.livedata.Event
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import br.com.sailboat.uicomponent.model.UiModel

internal class TaskDetailsViewState {
    val viewAction = Event<TaskDetailsViewAction>()
    val loading = MutableLiveData(true)
    val taskMetrics = MutableLiveData<TaskMetrics>()
    val taskDetails = MutableLiveData<List<UiModel>>()
    var taskId: Long = Entity.NO_ID
}
