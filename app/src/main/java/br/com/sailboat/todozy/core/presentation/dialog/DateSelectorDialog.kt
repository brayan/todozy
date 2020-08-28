package br.com.sailboat.todozy.core.presentation.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.FragmentManager
import br.com.sailboat.todozy.core.presentation.base.BaseDialogFragment
import java.util.*

class DateSelectorDialog(private val callback: Callback) : BaseDialogFragment(), DatePickerDialog.OnDateSetListener {


    interface Callback {
        fun onDateSet(year: Int, month: Int, day: Int)
    }

    private var calendar: Calendar? = null


    companion object {
        fun show(manager: FragmentManager, callback: Callback) {
            show(manager, null, callback)
        }

        fun show(manager: FragmentManager, calendar: Calendar?, callback: Callback) {
            val dialog = DateSelectorDialog(callback)
            dialog.calendar = calendar
            dialog.show(manager, "DATE_SELECTOR")
        }

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initCalendar()
        return DatePickerDialog(activity!!, this, getYear(), getMonth(), getDay())
    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        if (view.isShown) {
            callback.onDateSet(year, monthOfYear, dayOfMonth)
        }
    }

    private fun initCalendar() {
        if (calendar == null) {
            this.calendar = Calendar.getInstance()
        }
    }

    private fun getDay(): Int {
        return calendar!!.get(Calendar.DAY_OF_MONTH)
    }

    private fun getMonth(): Int {
        return calendar!!.get(Calendar.MONTH)
    }

    private fun getYear(): Int {
        return calendar!!.get(Calendar.YEAR)
    }
}