package br.com.sailboat.todozy.utility.android.dialog.dateselector

import java.util.Calendar

sealed class DateSelectorViewAction {
    data class OnStart(
        val calendar: Calendar?,
    ) : DateSelectorViewAction()
}
