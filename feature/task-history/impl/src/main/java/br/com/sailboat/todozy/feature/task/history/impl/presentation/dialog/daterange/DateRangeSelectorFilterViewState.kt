package br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.daterange

import androidx.lifecycle.MutableLiveData
import br.com.sailboat.todozy.utility.android.livedata.Event
import java.util.Calendar

internal class DateRangeSelectorFilterViewState {
    val action = Event<Action>()
    val initialDate = MutableLiveData<Calendar>()
    val finalDate = MutableLiveData<Calendar>()

    sealed class Action {
        data class ReturnSelectedDates(val initialDate: Calendar, val finalDate: Calendar) : Action()
        object ShowDateCantBeGreaterThanTodayMessage : Action()
        object ShowInitialDateCantBeGreaterThanFinalDateMessage : Action()
        object ShowFinalDateCantBeLowerThanFinalDateMessage : Action()
    }
}
