package br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.daterange

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.daterange.DateRangeSelectorFilterViewState.Action.ReturnSelectedDates
import br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.daterange.DateRangeSelectorFilterViewState.Action.ShowDateCantBeGreaterThanTodayMessage
import br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.daterange.DateRangeSelectorFilterViewState.Action.ShowFinalDateCantBeLowerThanFinalDateMessage
import br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.daterange.DateRangeSelectorFilterViewState.Action.ShowInitialDateCantBeGreaterThanFinalDateMessage
import br.com.sailboat.todozy.utility.android.calendar.toShortDateView
import br.com.sailboat.todozy.utility.android.fragment.BaseDialogFragment
import br.com.sailboat.todozy.utility.kotlin.extension.orNewCalendar
import br.com.sailboat.uicomponent.impl.databinding.DialogDateRangeSelectorBinding
import br.com.sailboat.uicomponent.impl.dialog.MessageDialog
import br.com.sailboat.uicomponent.impl.R as UiR
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar
import java.util.TimeZone

private const val INITIAL_DATE_SELECTOR = "INITIAL_DATE_SELECTOR"
private const val FINAL_DATE_SELECTOR = "FINAL_DATE_SELECTOR"

internal class DateRangeSelectorFilterDialog : BaseDialogFragment() {

    private var initialDate: Calendar? = null
    private var finalDate: Calendar? = null
    private var initialPicker: MaterialDatePicker<Long>? = null
    private var finalPicker: MaterialDatePicker<Long>? = null

    private lateinit var binding: DialogDateRangeSelectorBinding

    var callback: Callback? = null

    private val viewModel: DateRangeSelectorFilterViewModel by viewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogDateRangeSelectorBinding.inflate(LayoutInflater.from(requireContext()))

        initViews()
        observeViewModel()
        viewModel.dispatchViewIntent(DateRangeSelectorFilterViewAction.OnStart(initialDate, finalDate))

        return buildDialog()
    }

    override fun onResume() {
        super.onResume()
        reattachPickers()
    }

    private fun initViews() = with(binding) {
        llDateRangeSelectorInitialDate.setOnClickListener {
            if (childFragmentManager.findFragmentByTag(INITIAL_DATE_SELECTOR) != null) return@setOnClickListener

            val picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(UiR.string.label_starting_date))
                .setSelection(initialDate.orNewCalendar().timeInMillis)
                .build()

            picker.addOnPositiveButtonClickListener { millis ->
                handleInitialDateSelection(millis)
            }

            picker.show(childFragmentManager, INITIAL_DATE_SELECTOR)
            initialPicker = picker
        }

        llDateRangeSelectorFinalDate.setOnClickListener {
            if (childFragmentManager.findFragmentByTag(FINAL_DATE_SELECTOR) != null) return@setOnClickListener

            val picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(UiR.string.final_date))
                .setSelection(finalDate.orNewCalendar().timeInMillis)
                .build()

            picker.addOnPositiveButtonClickListener { millis ->
                handleFinalDateSelection(millis)
            }

            picker.show(childFragmentManager, FINAL_DATE_SELECTOR)
            finalPicker = picker
        }
    }

    private fun observeViewModel() {
        viewModel.viewState.action.observe(this) { action ->
            when (action) {
                is ReturnSelectedDates -> returnSelectedDates(action)
                is ShowDateCantBeGreaterThanTodayMessage -> showDateCantBeGreaterThanTodayMessage()
                is ShowInitialDateCantBeGreaterThanFinalDateMessage ->
                    showInitialDateCantBeGreaterThanFinalDateMessage()
                is ShowFinalDateCantBeLowerThanFinalDateMessage -> showFinalDateCantBeLowerThanFinalDateMessage()
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

    private fun showDateCantBeGreaterThanTodayMessage() {
        childFragmentManager.run {
            MessageDialog.showMessage(
                manager = this,
                message = "A data não pode ser maior que hoje",
                title = "Ops...",
            )
        }
    }

    private fun showInitialDateCantBeGreaterThanFinalDateMessage() {
        childFragmentManager.run {
            MessageDialog.showMessage(
                manager = this,
                message = "A data inicial não pode ser maior que a data final",
                title = "Ops...",
            )
        }
    }

    private fun showFinalDateCantBeLowerThanFinalDateMessage() {
        MessageDialog.showMessage(
            childFragmentManager,
            getString(UiR.string.msg_end_date),
            "Ops..."
        )
    }

    private fun returnSelectedDates(action: ReturnSelectedDates) {
        callback?.onClickOk(action.initialDate, action.finalDate)
    }

    private fun buildDialog(): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            viewModel.dispatchViewIntent(DateRangeSelectorFilterViewAction.OnClickConfirmSelectedDates)
        }

        builder.setNegativeButton(UiR.string.cancel, null)

        return builder.create()
    }

    private fun reattachPickers() {
        (childFragmentManager.findFragmentByTag(INITIAL_DATE_SELECTOR) as? MaterialDatePicker<*>)?.let { picker ->
            picker.clearOnPositiveButtonClickListeners()
            picker.addOnPositiveButtonClickListener { millis -> handleInitialDateSelection(millis) }
            initialPicker = picker as? MaterialDatePicker<Long>
        }

        (childFragmentManager.findFragmentByTag(FINAL_DATE_SELECTOR) as? MaterialDatePicker<*>)?.let { picker ->
            picker.clearOnPositiveButtonClickListeners()
            picker.addOnPositiveButtonClickListener { millis -> handleFinalDateSelection(millis) }
            finalPicker = picker as? MaterialDatePicker<Long>
        }
    }

    private fun handleInitialDateSelection(selection: Any?) {
        val millis = selection as? Long ?: return
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = millis }
        viewModel.dispatchViewIntent(
            DateRangeSelectorFilterViewAction.OnSelectInitialDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
            )
        )
        initialPicker = null
    }

    private fun handleFinalDateSelection(selection: Any?) {
        val millis = selection as? Long ?: return
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = millis }
        viewModel.dispatchViewIntent(
            DateRangeSelectorFilterViewAction.OnSelectFinalDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
            )
        )
        finalPicker = null
    }

    interface Callback {
        fun onClickOk(initialDate: Calendar, finalDate: Calendar)
    }

    companion object {
        val TAG: String = DateRangeSelectorFilterDialog::class.java.name

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
