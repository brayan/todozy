package br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import br.com.sailboat.todozy.feature.task.history.impl.databinding.DialogFilterTaskHistoryBinding
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.DateFilterTaskHistorySelectableItem
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.TaskStatusSelectableItem
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class TaskHistoryFilterDialog : DialogFragment() {
    var status: TaskStatusSelectableItem? = null
    var date: DateFilterTaskHistorySelectableItem? = null
    var callback: Callback? = null
    private lateinit var binding: DialogFilterTaskHistoryBinding

    private val viewModel: TaskHistoryFilterViewModel by viewModel()

    interface Callback {
        fun onClickFilterDate()
        fun onClickFilterStatus()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogFilterTaskHistoryBinding.inflate(LayoutInflater.from(requireContext()))
        initViews()
        observeViewModel()
        viewModel.dispatchViewIntent(TaskHistoryFilterViewAction.OnStart(date, status))
        return buildDialog()
    }

    private fun initViews() = with(binding) {
        llDialogFilterTaskHistoryDate.setOnClickListener {
            callback?.onClickFilterDate()
            dialog?.dismiss()
        }

        llDialogFilterTaskHistoryStatus.setOnClickListener {
            callback?.onClickFilterStatus()
            dialog?.dismiss()
        }
    }

    private fun observeViewModel() {
        viewModel.viewState.status.observe(this) { status ->
            status?.run { binding.tvDialogFilterTaskHistoryStatus.setText(getName()) }
        }
        viewModel.viewState.date.observe(this) { date ->
            date?.run { binding.tvDialogFilterTaskHistoryDate.setText(getName()) }
        }
    }

    private fun buildDialog() = AlertDialog.Builder(requireContext()).run {
        setView(binding.root)
        create()
    }

    companion object {
        val TAG = TaskHistoryFilterDialog::class.java.name

        fun show(
            manager: FragmentManager,
            date: DateFilterTaskHistorySelectableItem,
            status: TaskStatusSelectableItem,
            callback: Callback,
        ): TaskHistoryFilterDialog {
            val dialog = TaskHistoryFilterDialog()
            dialog.callback = callback
            dialog.status = status
            dialog.date = date
            dialog.show(manager, TAG)

            return dialog
        }
    }
}
