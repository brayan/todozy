package br.com.sailboat.todozy.utility.android.dialog.dateselector

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class DateSelectorDialog : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var calendar: Calendar? = null

    var callback: Callback? = null

    private val viewModel: DateSelectorViewModel by viewModel()

    interface Callback {
        fun onDateSelected(year: Int, month: Int, day: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeViewModel()
        viewModel.dispatchViewAction(DateSelectorViewAction.OnStart(calendar))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(requireContext(), this, getYear(), getMonth(), getDay())
    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        if (view.isShown) {
            callback?.onDateSelected(year, monthOfYear, dayOfMonth)
        }
    }

    private fun observeViewModel() {
        viewModel.viewState.calendar.observe(this) { calendar ->
            this.calendar = calendar
        }
    }

    private fun getDay(): Int {
        return calendar?.get(Calendar.DAY_OF_MONTH) ?: -1
    }

    private fun getMonth(): Int {
        return calendar?.get(Calendar.MONTH) ?: -1
    }

    private fun getYear(): Int {
        return calendar?.get(Calendar.YEAR) ?: -1
    }

    companion object {
        fun show(
            tag: String,
            fragmentManager: FragmentManager,
            calendar: Calendar,
            callback: Callback,
        ) = DateSelectorDialog().apply {
            this.calendar = calendar
            this.callback = callback
            show(fragmentManager, tag)
        }
    }
}
