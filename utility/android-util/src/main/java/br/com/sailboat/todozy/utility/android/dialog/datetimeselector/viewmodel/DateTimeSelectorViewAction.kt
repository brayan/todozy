package br.com.sailboat.todozy.utility.android.dialog.datetimeselector.viewmodel

import java.util.Calendar

sealed class DateTimeSelectorViewAction {
    data class OnStart(
        val calendar: Calendar?,
    ) : DateTimeSelectorViewAction()
}
