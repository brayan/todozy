package br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.daterange

import androidx.lifecycle.MutableLiveData
import java.util.Calendar

internal class DateRangeSelectorFilterViewState {
    val initialDate = MutableLiveData<Calendar>()
    val finalDate = MutableLiveData<Calendar>()
}
