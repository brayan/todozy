package br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog

import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel

internal class TaskHistoryFilterViewModel(
    override val viewState: TaskHistoryFilterViewState = TaskHistoryFilterViewState(),
) : BaseViewModel<TaskHistoryFilterViewState, TaskHistoryFilterViewAction>() {

    override fun dispatchViewAction(viewAction: TaskHistoryFilterViewAction) {
        when (viewAction) {
            is TaskHistoryFilterViewAction.OnStart -> onStart(viewAction)
        }
    }

    private fun onStart(viewAction: TaskHistoryFilterViewAction.OnStart) {
        viewAction.status?.run { viewState.status.value = this }
        viewAction.date?.run { viewState.date.value = this }
    }
}
