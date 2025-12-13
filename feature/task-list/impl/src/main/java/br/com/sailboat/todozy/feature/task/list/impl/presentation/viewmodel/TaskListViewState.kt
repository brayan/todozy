package br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel

import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskProgressDay
import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.uicomponent.model.UiModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

internal class TaskListViewState {
    val viewAction = MutableSharedFlow<TaskListViewAction>(replay = 1, extraBufferCapacity = 1)
    val tasksLoading = MutableStateFlow(false)
    val itemsView = MutableStateFlow<List<UiModel>>(emptyList())
    val taskMetrics = MutableStateFlow<TaskMetrics?>(null)
    val taskProgressDays = MutableStateFlow<List<TaskProgressDay>>(emptyList())
    val taskProgressRange = MutableStateFlow(TaskProgressRange.LAST_YEAR)
    val taskProgressLoading = MutableStateFlow(false)
}
