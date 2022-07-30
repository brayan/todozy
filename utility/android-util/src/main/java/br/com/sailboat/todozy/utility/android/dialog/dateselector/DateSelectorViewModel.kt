package br.com.sailboat.todozy.utility.android.dialog.dateselector

import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel

class DateSelectorViewModel(
    override val viewState: DateSelectorViewState = DateSelectorViewState(),
) : BaseViewModel<DateSelectorViewState, DateSelectorViewAction>() {

    override fun dispatchViewAction(viewAction: DateSelectorViewAction) {
        when (viewAction) {
            is DateSelectorViewAction.OnStart -> onStart(viewAction)
        }
    }

    private fun onStart(viewAction: DateSelectorViewAction.OnStart) {
        viewState.calendar.value = viewAction.calendar
    }
}
