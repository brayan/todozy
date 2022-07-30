package br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.daterange

import java.util.Calendar

internal sealed class DateRangeSelectorFilterViewAction {
    data class OnStart(
        val initialDate: Calendar?,
        val finalDate: Calendar?,
    ) : DateRangeSelectorFilterViewAction()

    data class OnSelectInitialDate(
        val year: Int,
        val month: Int,
        val day: Int,
    ) : DateRangeSelectorFilterViewAction()

    data class OnSelectFinalDate(
        val year: Int,
        val month: Int,
        val day: Int,
    ) : DateRangeSelectorFilterViewAction()

    object OnClickConfirmSelectedDates : DateRangeSelectorFilterViewAction()
}
