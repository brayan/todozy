package br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.daterange

import java.util.Calendar

internal sealed class DateRangeSelectorFilterViewAction {
    data class OnStart(
        val initialDate: Calendar?,
        val finalDate: Calendar?,
    ) : DateRangeSelectorFilterViewAction()

    data class OnSelectInitialDate(
        val initialDate: Calendar?,
    ) : DateRangeSelectorFilterViewAction()

    data class OnSelectFinalDate(
        val finalDate: Calendar?,
    ) : DateRangeSelectorFilterViewAction()
}
