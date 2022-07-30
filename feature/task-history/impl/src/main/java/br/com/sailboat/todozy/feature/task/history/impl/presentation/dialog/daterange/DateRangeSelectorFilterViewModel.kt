package br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.daterange

import br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.daterange.DateRangeSelectorFilterViewAction.OnClickConfirmSelectedDates
import br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.daterange.DateRangeSelectorFilterViewAction.OnSelectFinalDate
import br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.daterange.DateRangeSelectorFilterViewAction.OnSelectInitialDate
import br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.daterange.DateRangeSelectorFilterViewAction.OnStart
import br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.daterange.DateRangeSelectorFilterViewState.Action.ShowDateCantBeGreaterThanTodayMessage
import br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.daterange.DateRangeSelectorFilterViewState.Action.ShowFinalDateCantBeLowerThanFinalDateMessage
import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel
import br.com.sailboat.todozy.utility.kotlin.extension.clearTime
import br.com.sailboat.todozy.utility.kotlin.extension.isAfterToday
import br.com.sailboat.todozy.utility.kotlin.extension.orNewCalendar
import br.com.sailboat.todozy.utility.kotlin.extension.setFinalTimeToCalendar
import java.util.Calendar

internal class DateRangeSelectorFilterViewModel(
    override val viewState: DateRangeSelectorFilterViewState = DateRangeSelectorFilterViewState(),
) : BaseViewModel<DateRangeSelectorFilterViewState, DateRangeSelectorFilterViewAction>() {

    override fun dispatchViewAction(viewAction: DateRangeSelectorFilterViewAction) {
        when (viewAction) {
            is OnStart -> onStart(viewAction)
            is OnSelectInitialDate -> onSelectInitialDate(viewAction)
            is OnSelectFinalDate -> onSelectFinalDate(viewAction)
            is OnClickConfirmSelectedDates -> onClickConfirmSelectedDates()
        }
    }

    private fun onStart(viewAction: OnStart) {
        viewAction.initialDate?.run { viewState.initialDate.value = this }
        viewAction.finalDate?.run { viewState.finalDate.value = this }
    }

    private fun onSelectInitialDate(viewAction: OnSelectInitialDate) {
        val initialDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, viewAction.year)
            set(Calendar.MONTH, viewAction.month)
            set(Calendar.DAY_OF_MONTH, viewAction.day)
            clearTime()
        }

        if (initialDate.isAfterToday()) {
            viewState.action.value = ShowDateCantBeGreaterThanTodayMessage
            return
        }

        if (initialDate.after(viewState.finalDate.value)) {
            viewState.action.value =
                DateRangeSelectorFilterViewState.Action.ShowInitialDateCantBeGreaterThanFinalDateMessage
            return
        }

        viewState.initialDate.value = initialDate
    }

    private fun onSelectFinalDate(viewAction: OnSelectFinalDate) {
        val finalDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, viewAction.year)
            set(Calendar.MONTH, viewAction.month)
            set(Calendar.DAY_OF_MONTH, viewAction.day)
            setFinalTimeToCalendar()
        }

        if (finalDate.isAfterToday()) {
            viewState.action.value = ShowDateCantBeGreaterThanTodayMessage
            return
        }

        if (finalDate.before(viewState.initialDate.value)) {
            viewState.action.value = ShowFinalDateCantBeLowerThanFinalDateMessage
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
