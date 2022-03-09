package br.com.sailboat.todozy.feature.task.history.impl.presentation

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import br.com.sailboat.todozy.feature.task.history.impl.databinding.DlgFilterTaskHistoryBinding
import br.com.sailboat.todozy.uicomponent.dialog.selectable.model.DateFilterTaskHistorySelectableItem
import br.com.sailboat.todozy.uicomponent.dialog.selectable.model.TaskStatusSelectableItem

class TaskHistoryFilterDialog : DialogFragment() {

    var status: TaskStatusSelectableItem? = null
    var date: DateFilterTaskHistorySelectableItem? = null
    private var callback: Callback? = null
    private lateinit var binding: DlgFilterTaskHistoryBinding

    interface Callback {
        fun onClickFilterDate()
        fun onClickFilterStatus()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DlgFilterTaskHistoryBinding.inflate(LayoutInflater.from(requireContext()))
        initViews()
        updateViews()
        return buildDialog()
    }

    private fun initViews() = with(binding) {
        tvDate.setOnClickListener {
            callback?.onClickFilterDate()
            dialog?.dismiss()
        }

        tvStatus.setOnClickListener {
            callback?.onClickFilterStatus()
            dialog?.dismiss()
        }
    }

    private fun updateViews() = with(binding) {
        date?.run { tvDate.setText(getName()) }
        status?.run { tvStatus.setText(getName()) }
    }

    private fun buildDialog() =
        AlertDialog.Builder(requireContext()).run {
            setView(binding.root)
            create()
        }

    companion object {
        fun show(
            manager: FragmentManager, date: DateFilterTaskHistorySelectableItem,
            status: TaskStatusSelectableItem, callback: Callback
        ) {
            val dialog = TaskHistoryFilterDialog()
            dialog.callback = callback
            dialog.status = status
            dialog.date = date
            dialog.show(manager, TaskHistoryFilterDialog::class.java.name)
        }
    }
}