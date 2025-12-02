package br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskProgressDay
import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.todozy.utility.android.livedata.Event
import br.com.sailboat.uicomponent.model.UiModel

internal class TaskListViewState {
    val viewAction = Event<TaskListViewAction>()
    val tasksLoading = MutableLiveData(false)
    val itemsView = MutableLiveData<MutableList<UiModel>>()
    val taskMetrics = MutableLiveData<TaskMetrics>()
    val taskProgressDays = MutableLiveData<List<TaskProgressDay>>(emptyList())
    val taskProgressRange = MutableLiveData(TaskProgressRange.LAST_YEAR)
    val taskProgressLoading = MutableLiveData(false)
}
