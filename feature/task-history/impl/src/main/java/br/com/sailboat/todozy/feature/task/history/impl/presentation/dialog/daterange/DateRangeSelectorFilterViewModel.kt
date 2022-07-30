package br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.daterange

import br.com.sailboat.todozy.feature.task.history.impl.R
import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel
import br.com.sailboat.todozy.utility.kotlin.extension.clearTime
import br.com.sailboat.todozy.utility.kotlin.extension.isAfterToday
import br.com.sailboat.todozy.utility.kotlin.extension.orNewCalendar
import br.com.sailboat.todozy.utility.kotlin.extension.setFinalTimeToCalendar
import br.com.sailboat.uicomponent.impl.dialog.MessageDialog
import java.util.Calendar

internal class DateRangeSelectorFilterViewModel(
    override val viewState: DateRangeSelectorFilterViewState = DateRangeSelectorFilterViewState(),
) : BaseViewModel<DateRangeSelectorFilterViewState, DateRangeSelectorFilterViewAction>() {

    override fun dispatchViewAction(viewAction: DateRangeSelectorFilterViewAction) {
        when (viewAction) {
            is DateRangeSelectorFilterViewAction.OnStart -> onStart(viewAction)
            is DateRangeSelectorFilterViewAction.OnSelectInitialDate -> onSelectInitialDate(viewAction)
            is DateRangeSelectorFilterViewAction.OnSelectFinalDate -> onSelectFinalDate(viewAction)
            is DateRangeSelectorFilterViewAction.OnClickConfirmSelectedDates -> onClickConfirmSelectedDates()
        }
    }

    private fun onStart(viewAction: DateRangeSelectorFilterViewAction.OnStart) {
        viewAction.initialDate?.run { viewState.initialDate.value = this }
        viewAction.finalDate?.run { viewState.finalDate.value = this }
    }

    private fun onSelectInitialDate(viewAction: DateRangeSelectorFilterViewAction.OnSelectInitialDate) {
        val initialDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, viewAction.year)
            set(Calendar.MONTH, viewAction.month)
            set(Calendar.DAY_OF_MONTH, viewAction.day)
            clearTime()
        }

        if (initialDate.isAfterToday()) {
            viewState.action.value = DateRangeSelectorFilterViewState.Action.ShowDateCantBeGreaterThanTodayMessage
            return
        }

        if (initialDate.after(viewState.finalDate.value)) {
            viewState.action.value = DateRangeSelectorFilterViewState.Action.ShowInitialDateCantBeGreaterThanFinalDateMessage
            return
        }

        viewState.initialDate.value = initialDate
    }

    private fun onSelectFinalDate(viewAction: DateRangeSelectorFilterViewAction.OnSelectFinalDate) {
        val finalDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, viewAction.year)
            set(Calendar.MONTH, viewAction.month)
            set(Calendar.DAY_OF_MONTH, viewAction.day)
            setFinalTimeToCalendar()
        }

        if (finalDate.isAfterToday()) {
            viewState.action.value = DateRangeSelectorFilterViewState.Action.ShowDateCantBeGreaterThanTodayMessage
            return
        }

        if (finalDate.before(viewState.initialDate.value)) {
            viewState.action.value = DateRangeSelectorFilterViewState.Action.ShowFinalDateCantBeLowerThanFinalDateMessage
            return
        }

        viewState.finalDate.value = finalDate
    }

    private fun onClickConfirmSelectedDates() {
        viewState.action.value = DateRangeSelectorFilterViewState.Action.ReturnSelectedDates(
            initialDate = viewState.initialDate.value.orNewCalendar(),
            finalDate = viewState.finalDate.value.orNewCalendar(),
        )
    }
}
