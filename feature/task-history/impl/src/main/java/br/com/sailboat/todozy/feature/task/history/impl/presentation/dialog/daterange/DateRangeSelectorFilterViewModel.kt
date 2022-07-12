package br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.daterange

import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel

internal class DateRangeSelectorFilterViewModel(
    override val viewState: DateRangeSelectorFilterViewState = DateRangeSelectorFilterViewState(),
) : BaseViewModel<DateRangeSelectorFilterViewState, DateRangeSelectorFilterViewAction>() {

    override fun dispatchViewAction(viewAction: DateRangeSelectorFilterViewAction) {
        when (viewAction) {
            is DateRangeSelectorFilterViewAction.OnStart -> onStart(viewAction)
            is DateRangeSelectorFilterViewAction.OnSelectInitialDate -> onSelectInitialDate(viewAction)
            is DateRangeSelectorFilterViewAction.OnSelectFinalDate -> onSelectFinalDate(viewAction)
        }
    }

    private fun onSelectInitialDate(viewAction: DateRangeSelectorFilterViewAction.OnSelectInitialDate) {
        viewAction.initialDate?.run { viewState.initialDate.value = this }
    }

    private fun onSelectFinalDate(viewAction: DateRangeSelectorFilterViewAction.OnSelectFinalDate) {
        viewAction.finalDate?.run { viewState.finalDate.value = this }
    }

    private fun onStart(viewAction: DateRangeSelectorFilterViewAction.OnStart) {
        viewAction.initialDate?.run { viewState.initialDate.value = this }
        viewAction.finalDate?.run { viewState.finalDate.value = this }
    }
}
