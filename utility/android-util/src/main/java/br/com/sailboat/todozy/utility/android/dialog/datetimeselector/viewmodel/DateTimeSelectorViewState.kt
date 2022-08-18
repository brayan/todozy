package br.com.sailboat.todozy.utility.android.dialog.datetimeselector.viewmodel

import androidx.lifecycle.MutableLiveData
import java.util.Calendar

class DateTimeSelectorViewState {
    val calendar = MutableLiveData<Calendar>()
}
