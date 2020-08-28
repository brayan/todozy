package br.com.sailboat.todozy.core.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.extensions.clearTime
import br.com.sailboat.todozy.core.extensions.isAfterToday
import br.com.sailboat.todozy.core.extensions.setFinalTimeToCalendar
import br.com.sailboat.todozy.core.presentation.base.BaseDialogFragment
import br.com.sailboat.todozy.core.presentation.helper.toShortDateView
import java.util.*

class DateRangeSelectorDialog : BaseDialogFragment() {


    private var initialDate: Calendar? = null
    private var finalDate: Calendar? = null

    private var tvInitialDate: TextView? = null
    private var tvFinalDate: TextView? = null

    private var callback: Callback? = null

    interface Callback {
        fun onClickOk(initialDate: Calendar, finalDate: Calendar)
    }

    companion object {
        fun show(manager: FragmentManager, callback: Callback) {
            show(manager, null, null, callback)
        }

        fun show(manager: FragmentManager, initialDate: Calendar?, finalDate: Calendar?, callback: Callback) {
            val dialog = DateRangeSelectorDialog()
            dialog.callback = callback
            dialog.initialDate = initialDate
            dialog.finalDate = finalDate
            dialog.show(manager, DateRangeSelectorDialog::class.java.name)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDates()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(activity, R.layout.dlg_date_range_selector, null)
        initViews(view)
        updateViews()

        return construirDialog(view)
    }

    private fun initViews(view: View) {
        tvInitialDate = view.findViewById<View>(R.id.dlg_date_range_selector__tv__initial) as TextView
        tvFinalDate = view.findViewById<View>(R.id.dlg_date_range_selector__tv__final) as TextView

        view.findViewById<View>(R.id.dlg_date_range_selector__ll__initial).setOnClickListener {
            DateSelectorDialog.show(fragmentManager!!, initialDate, object : DateSelectorDialog.Callback {
                override fun onDateSet(year: Int, month: Int, day: Int) {
                    val newInitialDate = Calendar.getInstance()
                    newInitialDate.set(Calendar.YEAR, year)
                    newInitialDate.set(Calendar.MONTH, month)
                    newInitialDate.set(Calendar.DAY_OF_MONTH, day)
                    newInitialDate.clearTime()

                    if (newInitialDate.isAfterToday()) {
                        fragmentManager?.run { MessageDialog.showMessage(this, "A data não pode ser maior que hoje", "Ops...") }
                        return
                    }

                    if (newInitialDate.after(finalDate)) {
                        fragmentManager?.run { MessageDialog.showMessage(this, "A data inicial não pode ser maior que a data final", "Ops...") }
                        return
                    }

                    initialDate!!.set(Calendar.YEAR, year)
                    initialDate!!.set(Calendar.MONTH, month)
                    initialDate!!.set(Calendar.DAY_OF_MONTH, day)

                    updateViews()
                }
            })
        }

        view.findViewById<View>(R.id.dlg_date_range_selector__ll__final).setOnClickListener {
            DateSelectorDialog.show(fragmentManager!!, finalDate, object : DateSelectorDialog.Callback {
                override fun onDateSet(year: Int, month: Int, day: Int) {
                    val newFinalDate = Calendar.getInstance()
                    newFinalDate.set(Calendar.YEAR, year)
                    newFinalDate.set(Calendar.MONTH, month)
                    newFinalDate.set(Calendar.DAY_OF_MONTH, day)
                    newFinalDate.setFinalTimeToCalendar()

                    if (newFinalDate.isAfterToday()) {
                        MessageDialog.showMessage(fragmentManager!!, getString(R.string.msg_data_cant_be_greater), "Ops...")
                        return
                    }

                    if (newFinalDate.before(initialDate)) {
                        MessageDialog.showMessage(fragmentManager!!, getString(R.string.msg_end_date), "Ops...")
                        return
                    }

                    finalDate!!.set(Calendar.YEAR, year)
                    finalDate!!.set(Calendar.MONTH, month)
                    finalDate!!.set(Calendar.DAY_OF_MONTH, day)

                    updateViews()
                }
            })
        }
    }

    private fun updateViews() {
        tvInitialDate!!.setText(initialDate?.toShortDateView(activity!!))
        tvFinalDate!!.setText(finalDate?.toShortDateView(activity!!))
    }

    private fun construirDialog(view: View): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        builder.setView(view)
        builder.setPositiveButton(android.R.string.ok) { _, _ -> callback!!.onClickOk(initialDate!!, finalDate!!) }

        builder.setNegativeButton(R.string.cancel, null)

        return builder.create()
    }

    private fun initDates() {
        if (initialDate == null) {
            initialDate = Calendar.getInstance()
            initialDate!!.clearTime()
        }

        if (finalDate == null) {
            finalDate = Calendar.getInstance()
            finalDate!!.setFinalTimeToCalendar()
        }
    }

}