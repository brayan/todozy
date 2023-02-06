package br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.utility.android.livedata.Event
import br.com.sailboat.uicomponent.model.UiModel

internal class TaskListViewState {
    val viewAction = Event<TaskListViewAction>()
    val loading = MutableLiveData(true)
    val itemsView = MutableLiveData<MutableList<UiModel>>()
    val taskMetrics = MutableLiveData<TaskMetrics>()
}
