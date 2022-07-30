package br.com.sailboat.todozy.utility.android.dialog.dateselector

import androidx.lifecycle.MutableLiveData
import java.util.Calendar

class DateSelectorViewState {
    val calendar = MutableLiveData<Calendar>()
}
