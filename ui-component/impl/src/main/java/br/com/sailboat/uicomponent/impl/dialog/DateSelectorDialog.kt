package br.com.sailboat.uicomponent.impl.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import java.util.*

class DateSelectorDialog(private val callback: Callback) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    private lateinit var calendar: Calendar

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(requireContext(), this, getYear(), getMonth(), getDay())
    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        if (view.isShown) {
            callback.onDateSet(year, monthOfYear, dayOfMonth)
        }
    }

    private fun getDay(): Int {
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    private fun getMonth(): Int {
        return calendar.get(Calendar.MONTH)
    }

    private fun getYear(): Int {
        return calendar.get(Calendar.YEAR)
    }

    interface Callback {
        fun onDateSet(year: Int, month: Int, day: Int)
    }

    companion object {
        fun show(fragmentManager: FragmentManager, callback: Callback) {
            show(fragmentManager, Calendar.getInstance(), callback)
        }

        fun show(fragmentManager: FragmentManager, calendar: Calendar, callback: Callback) {
            val dialog = DateSelectorDialog(callback)
            dialog.calendar = calendar
            dialog.show(fragmentManager, DateSelectorDialog::class.java.name)
        }
    }
}