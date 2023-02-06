package br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.utility.android.livedata.Event
import br.com.sailboat.uicomponent.model.UiModel

internal class TaskHistoryViewState {
    val action = Event<TaskHistoryViewAction>()
    val loading = MutableLiveData(true)
    val taskHistoryList = MutableLiveData<List<UiModel>>()
    val taskMetrics = MutableLiveData<TaskMetrics>()
    val subtitle = MutableLiveData<String>()
}
