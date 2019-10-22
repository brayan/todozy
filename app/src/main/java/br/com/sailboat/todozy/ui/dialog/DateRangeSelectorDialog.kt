package br.com.sailboat.todozy.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.domain.helper.clearTime
import br.com.sailboat.todozy.domain.helper.isAfterToday
import br.com.sailboat.todozy.domain.helper.setFinalTimeToCalendar
import br.com.sailboat.todozy.ui.base.BaseDialogFragment
import br.com.sailboat.todozy.ui.helper.toShortDateView
import java.util.*

class DateRangeSelectorDialog : BaseDialogFragment() {


    private var initialDate: Calendar? = null
    private var finalDate: Calendar? = null

    private var tvInitialDate: TextView? = null
    private var tvFinalDate: TextView? = null

    private var callback: Callback? = null

    companion object {
        fun show(manager: FragmentManager, callback: Callback) {
            show(manager, null, null, callback)
        }

        fun show(manager: FragmentManager, initialDate: Calendar?, finalDate: Calendar?, callback: Callback) {
            val dialog = DateRangeSelectorDialog()
            dialog.callback = callback
            dialog.setInitialDate(initialDate)
            dialog.setFinalDate(finalDate)
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
            DateSelectorDialog.show(fragmentManager!!, getInitialDate(), object : DateSelectorDialog.Callback {
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

                    getInitialDate()!!.set(Calendar.YEAR, year)
                    getInitialDate()!!.set(Calendar.MONTH, month)
                    getInitialDate()!!.set(Calendar.DAY_OF_MONTH, day)

                    updateViews()
                }
            })
        }

        view.findViewById<View>(R.id.dlg_date_range_selector__ll__final).setOnClickListener {
            DateSelectorDialog.show(fragmentManager!!, getFinalDate(), object : DateSelectorDialog.Callback {
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

                    getFinalDate()!!.set(Calendar.YEAR, year)
                    getFinalDate()!!.set(Calendar.MONTH, month)
                    getFinalDate()!!.set(Calendar.DAY_OF_MONTH, day)

                    updateViews()
                }
            })
        }
    }

    private fun updateViews() {
        tvInitialDate!!.setText(getInitialDate()?.toShortDateView(activity!!))
        tvFinalDate!!.setText(getFinalDate()?.toShortDateView(activity!!))
    }

    private fun construirDialog(view: View): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        builder.setView(view)
        builder.setPositiveButton(android.R.string.ok) { dialog, which -> callback!!.onClickOk(getInitialDate(), getFinalDate()) }

        builder.setNegativeButton(R.string.cancel, null)

        return builder.create()
    }

    private fun initDates() {
        if (initialDate == null) {
            initialDate = Calendar.getInstance()
        }
        initialDate!!.clearTime()

        if (finalDate == null) {
            finalDate = Calendar.getInstance()
        }
        finalDate!!.setFinalTimeToCalendar()
    }

    fun getInitialDate(): Calendar? {
        return initialDate
    }

    fun setInitialDate(initialDate: Calendar?) {
        this.initialDate = initialDate
    }

    fun getFinalDate(): Calendar? {
        return finalDate
    }

    fun setFinalDate(finalDate: Calendar?) {
        this.finalDate = finalDate
    }


    interface Callback {
        fun onClickOk(initialDate: Calendar?, finalDate: Calendar?)
    }
}