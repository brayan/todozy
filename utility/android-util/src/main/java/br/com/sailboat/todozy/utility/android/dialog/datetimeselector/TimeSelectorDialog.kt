package br.com.sailboat.todozy.utility.android.dialog.datetimeselector

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import br.com.sailboat.todozy.utility.android.dialog.datetimeselector.viewmodel.DateTimeSelectorViewAction
import br.com.sailboat.todozy.utility.android.dialog.datetimeselector.viewmodel.DateTimeSelectorViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class TimeSelectorDialog : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var calendar: Calendar? = null

    var callback: Callback? = null

    private val viewModel: DateTimeSelectorViewModel by viewModel()

    interface Callback {
        fun onTimeSelected(hourOfDay: Int, minute: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeViewModel()
        viewModel.dispatchViewIntent(DateTimeSelectorViewAction.OnStart(calendar))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(
            requireContext(),
            this,
            getHour(),
            getMinute(),
            DateFormat.is24HourFormat(requireContext()),
        )
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        if (view.isShown) {
            callback?.onTimeSelected(hourOfDay, minute)
        }
    }

    private fun observeViewModel() {
        viewModel.viewState.calendar.observe(this) { calendar ->
            this.calendar = calendar
        }
    }

    private fun getMinute(): Int {
        return calendar?.get(Calendar.MINUTE) ?: -1
    }

    private fun getHour(): Int {
        return calendar?.get(Calendar.HOUR_OF_DAY) ?: -1
    }

    companion object {
        val TAG: String = TimeSelectorDialog::class.java.name

        fun show(
            fragmentManager: FragmentManager,
            calendar: Calendar,
            callback: Callback,
            tag: String = TAG,
        ) = TimeSelectorDialog().apply {
            this.calendar = calendar
            this.callback = callback
            show(fragmentManager, tag)
        }
    }
}
