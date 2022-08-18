package br.com.sailboat.todozy.utility.android.dialog.datetimeselector.viewmodel

import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel

class DateTimeSelectorViewModel(
    override val viewState: DateTimeSelectorViewState = DateTimeSelectorViewState(),
) : BaseViewModel<DateTimeSelectorViewState, DateTimeSelectorViewAction>() {

    override fun dispatchViewAction(viewAction: DateTimeSelectorViewAction) {
        when (viewAction) {
            is DateTimeSelectorViewAction.OnStart -> onStart(viewAction)
        }
    }

    private fun onStart(viewAction: DateTimeSelectorViewAction.OnStart) {
        viewAction.calendar?.run { viewState.calendar.value = this }
    }
}
