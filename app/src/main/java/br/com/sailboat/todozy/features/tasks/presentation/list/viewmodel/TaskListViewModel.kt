package br.com.sailboat.todozy.features.tasks.presentation.list.viewmodel

import br.com.sailboat.todozy.core.presentation.base.BaseViewModel
import br.com.sailboat.todozy.features.tasks.domain.usecase.CompleteTaskUseCase
import br.com.sailboat.todozy.features.tasks.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.GetAlarmUseCase
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.ScheduleAllAlarmsUseCase
import br.com.sailboat.todozy.features.tasks.presentation.list.GetTasksViewUseCase
import br.com.sailboat.todozy.features.tasks.presentation.list.viewmodel.TaskListViewAction.*

class TaskListViewModel(
    private val getTasksViewUseCase: GetTasksViewUseCase,
    private val getAlarmUseCase: GetAlarmUseCase,
    private val scheduleAllAlarmsUseCase: ScheduleAllAlarmsUseCase,
    private val getTaskMetricsUseCase: GetTaskMetricsUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
) : BaseViewModel<TaskListViewState, TaskListViewAction>() {

    override val viewState = TaskListViewState()

    override fun dispatchViewAction(viewAction: TaskListViewAction) {
        when (viewAction) {
            OnClickMenuAbout -> onClickMenuAbout()
            OnClickMenuSettings -> onClickMenuSettings()
            OnClickMenuHistory -> onClickMenuHistory()
        }
    }

    private fun onClickMenuAbout() {
        viewState.action.value = TaskListViewState.Action.NavigateToAbout
    }

    private fun onClickMenuSettings() {
        viewState.action.value = TaskListViewState.Action.NavigateToSettings
    }

    private fun onClickMenuHistory() {
        viewState.action.value = TaskListViewState.Action.NavigateToHistory
    }

}