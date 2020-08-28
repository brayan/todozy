package br.com.sailboat.todozy.core.presentation.dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.FragmentManager
import br.com.sailboat.todozy.core.presentation.base.BaseDialogFragment
import java.util.*

class TimeSelectorDialog(private val callback: Callback) : BaseDialogFragment(), TimePickerDialog.OnTimeSetListener {


    var calendar: Calendar? = null

    companion object {
        fun show(fragmentManager: FragmentManager, callback: Callback) {
            show(fragmentManager, null, callback)
        }

        fun show(fragmentManager: FragmentManager, currentTime: Calendar?, callback: Callback) {
            val dialog = TimeSelectorDialog(callback)
            dialog.calendar = currentTime
            dialog.show(fragmentManager, TimeSelectorDialog::class.java.name)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initTime()

        return TimePickerDialog(activity, this, getHour(), getMinute(),
                DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        if (view.isShown) {
            callback.onTimeSet(hourOfDay, minute)
        }
    }

    private fun initTime() {
        if (calendar == null) {
            this.calendar = Calendar.getInstance()
        }
    }

    private fun getMinute(): Int {
        return calendar!!.get(Calendar.MINUTE)
    }

    private fun getHour(): Int {
        return calendar!!.get(Calendar.HOUR_OF_DAY)
    }


    interface Callback {
        fun onTimeSet(hourOfDay: Int, minute: Int)
    }

}