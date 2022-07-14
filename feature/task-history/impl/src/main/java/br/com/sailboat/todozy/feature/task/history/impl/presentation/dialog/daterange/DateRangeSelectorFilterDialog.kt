package br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.daterange

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import br.com.sailboat.todozy.feature.task.history.impl.R
import br.com.sailboat.todozy.utility.android.calendar.toShortDateView
import br.com.sailboat.todozy.utility.android.fragment.BaseDialogFragment
import br.com.sailboat.todozy.utility.kotlin.extension.clearTime
import br.com.sailboat.todozy.utility.kotlin.extension.isAfterToday
import br.com.sailboat.todozy.utility.kotlin.extension.orNewCalendar
import br.com.sailboat.todozy.utility.kotlin.extension.setFinalTimeToCalendar
import br.com.sailboat.uicomponent.impl.databinding.DialogDateRangeSelectorBinding
import br.com.sailboat.uicomponent.impl.dialog.DateSelectorDialog
import br.com.sailboat.uicomponent.impl.dialog.MessageDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

internal class DateRangeSelectorFilterDialog : BaseDialogFragment() {

    private var initialDate: Calendar? = null
    private var finalDate: Calendar? = null

    private lateinit var binding: DialogDateRangeSelectorBinding

    var callback: Callback? = null

    private val viewModel: DateRangeSelectorFilterViewModel by viewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogDateRangeSelectorBinding.inflate(LayoutInflater.from(requireContext()))

        initViews()
        observeViewModel()
        viewModel.dispatchViewAction(DateRangeSelectorFilterViewAction.OnStart(initialDate, finalDate))

        return buildDialog()
    }

    private fun initViews() = with(binding) {
        llDateRangeSelectorInitialDate.setOnClickListener {
            DateSelectorDialog.show(
                childFragmentManager,
                initialDate.orNewCalendar(),
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

                        viewModel.dispatchViewAction(
                            DateRangeSelectorFilterViewAction.OnSelectInitialDate(
                                newInitialDate
                            )
                        )
                    }
                }
            )
        }

        llDateRangeSelectorFinalDate.setOnClickListener {
            DateSelectorDialog.show(
                childFragmentManager,
                finalDate.orNewCalendar(),
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

                        viewModel.dispatchViewAction(DateRangeSelectorFilterViewAction.OnSelectFinalDate(newFinalDate))
                    }
                }
            )
        }
    }

    private fun observeViewModel() {
        viewModel.viewState.action.observe(this) { action ->
            when (action) {
                is DateRangeSelectorFilterViewState.Action.ReturnSelectedDates -> returnSelectedDates(action)
            }
        }
        viewModel.viewState.initialDate.observe(this) { initialDate ->
            initialDate?.run {
                binding.tvDateRangeSelectorInitialDate.text = initialDate.toShortDateView(requireContext())
            }
        }
        viewModel.viewState.finalDate.observe(this) { finalDate ->
            finalDate?.run {
                binding.tvDateRangeSelectorFinalDate.text = finalDate.toShortDateView(requireContext())
            }
        }
    }

    private fun returnSelectedDates(action: DateRangeSelectorFilterViewState.Action.ReturnSelectedDates) {
        callback?.onClickOk(action.initialDate, action.finalDate)
    }

    private fun buildDialog(): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            viewModel.dispatchViewAction(DateRangeSelectorFilterViewAction.OnClickConfirmSelectedDates)
        }

        builder.setNegativeButton(R.string.cancel, null)

        return builder.create()
    }

    interface Callback {
        fun onClickOk(initialDate: Calendar, finalDate: Calendar)
    }

    companion object {
        val TAG: String = DateRangeSelectorFilterDialog::class.java.name

        fun show(manager: FragmentManager, callback: Callback): DateRangeSelectorFilterDialog {
            return show(manager, null, null, callback)
        }

        fun show(
            manager: FragmentManager,
            initialDate: Calendar?,
            finalDate: Calendar?,
            callback: Callback
        ): DateRangeSelectorFilterDialog {
            val dialog = DateRangeSelectorFilterDialog()
            dialog.callback = callback
            dialog.initialDate = initialDate.orNewCalendar()
            dialog.finalDate = finalDate.orNewCalendar()
            dialog.show(manager, TAG)

            return dialog
        }
    }
}
