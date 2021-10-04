package br.com.sailboat.todozy.core.presentation.dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import java.util.*

class TimeSelectorDialog(private val callback: Callback) : DialogFragment(),
    TimePickerDialog.OnTimeSetListener {

    private lateinit var calendar: Calendar

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(
            activity,
            this,
            getHour(),
            getMinute(),
            DateFormat.is24HourFormat(activity),
        )
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        if (view.isShown) {
            callback.onTimeSet(hourOfDay, minute)
        }
    }

    private fun getMinute(): Int {
        return calendar.get(Calendar.MINUTE)
    }

    private fun getHour(): Int {
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    interface Callback {
        fun onTimeSet(hourOfDay: Int, minute: Int)
    }

    companion object {
        fun show(fragmentManager: FragmentManager, callback: Callback) {
            show(fragmentManager, Calendar.getInstance(), callback)
        }

        fun show(fragmentManager: FragmentManager, calendar: Calendar, callback: Callback) {
            val dialog = TimeSelectorDialog(callback)
            dialog.calendar = calendar
            dialog.show(fragmentManager, TimeSelectorDialog::class.java.name)
        }
    }
}