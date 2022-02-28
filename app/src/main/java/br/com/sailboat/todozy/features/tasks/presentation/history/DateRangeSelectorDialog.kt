package br.com.sailboat.todozy.features.tasks.presentation.history

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.uicomponent.databinding.DlgDateRangeSelectorBinding
import br.com.sailboat.todozy.uicomponent.dialog.DateSelectorDialog
import br.com.sailboat.todozy.uicomponent.dialog.MessageDialog
import br.com.sailboat.todozy.utility.android.calendar.toShortDateView
import br.com.sailboat.todozy.utility.android.fragment.BaseDialogFragment
import br.com.sailboat.todozy.utility.kotlin.extension.*
import java.util.*

class DateRangeSelectorDialog : BaseDialogFragment() {

    private lateinit var initialDate: Calendar
    private lateinit var finalDate: Calendar

    private lateinit var binding: DlgDateRangeSelectorBinding

    private var callback: Callback? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DlgDateRangeSelectorBinding.inflate(LayoutInflater.from(requireContext()))

        initViews()
        updateViews()

        return buildDialog()
    }

    private fun initViews() = with(binding) {
        llDateRangeSelectorInitialDate.setOnClickListener {
            DateSelectorDialog.show(
                childFragmentManager,
                initialDate,
                object : DateSelectorDialog.Callback {
                    override fun onDateSet(year: Int, month: Int, day: Int) {
                        val newInitialDate = Calendar.getInstance()
                        newInitialDate.set(Calendar.YEAR, year)
                        newInitialDate.set(Calendar.MONTH, month)
                        newInitialDate.set(Calendar.DAY_OF_MONTH, day)
                        newInitialDate.clearTime()

                        if (newInitialDate.isAfterToday()) {
                            childFragmentManager.run {
                                MessageDialog.showMessage(
                                    manager = this,
                                    message = "A data não pode ser maior que hoje",
                                    title = "Ops...",
                                )
                            }
                            return
                        }

                        if (newInitialDate.after(finalDate)) {
                            childFragmentManager.run {
                                MessageDialog.showMessage(
                                    manager = this,
                                    message = "A data inicial não pode ser maior que a data final",
                                    title = "Ops...",
                                )
                            }
                            return
                        }

                        initialDate.set(Calendar.YEAR, year)
                        initialDate.set(Calendar.MONTH, month)
                        initialDate.set(Calendar.DAY_OF_MONTH, day)

                        updateViews()
                    }
                })
        }

        llDateRangeSelectorFinalDate.setOnClickListener {
            DateSelectorDialog.show(
                childFragmentManager,
                finalDate,
                object : DateSelectorDialog.Callback {
                    override fun onDateSet(year: Int, month: Int, day: Int) {
                        val newFinalDate = Calendar.getInstance()
                        newFinalDate.set(Calendar.YEAR, year)
                        newFinalDate.set(Calendar.MONTH, month)
                        newFinalDate.set(Calendar.DAY_OF_MONTH, day)
                        newFinalDate.setFinalTimeToCalendar()

                        if (newFinalDate.isAfterToday()) {
                            MessageDialog.showMessage(
                                childFragmentManager,
                                getString(R.string.msg_data_cant_be_greater),
                                "Ops..."
                            )
                            return
                        }

                        if (newFinalDate.before(initialDate)) {
                            MessageDialog.showMessage(
                                childFragmentManager,
                                getString(R.string.msg_end_date),
                                "Ops..."
                            )
                            return
                        }

                        finalDate.set(Calendar.YEAR, year)
                        finalDate.set(Calendar.MONTH, month)
                        finalDate.set(Calendar.DAY_OF_MONTH, day)

                        updateViews()
                    }
                })
        }
    }

    private fun updateViews() = with(binding) {
        tvDateRangeSelectorInitialDate.text = initialDate.toShortDateView(requireContext())
        tvDateRangeSelectorFinalDate.text = finalDate.toShortDateView(requireContext())
    }

    private fun buildDialog(): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            callback?.onClickOk(initialDate, finalDate)
        }

        builder.setNegativeButton(R.string.cancel, null)

        return builder.create()
    }

    interface Callback {
        fun onClickOk(initialDate: Calendar, finalDate: Calendar)
    }

    companion object {
        fun show(manager: FragmentManager, callback: Callback) {
            show(manager, null, null, callback)
        }

        fun show(
            manager: FragmentManager,
            initialDate: Calendar?,
            finalDate: Calendar?,
            callback: Callback
        ) {
            val dialog = DateRangeSelectorDialog()
            dialog.callback = callback
            dialog.initialDate = initialDate.orNewCalendar()
            dialog.finalDate = finalDate.orNewCalendar()
            dialog.show(manager, DateRangeSelectorDialog::class.java.name)

            true.orFalse()
        }
    }

}