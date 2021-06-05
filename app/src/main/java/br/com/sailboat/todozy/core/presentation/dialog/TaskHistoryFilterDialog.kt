package br.com.sailboat.todozy.core.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.presentation.base.BaseDialogFragment
import br.com.sailboat.todozy.core.presentation.dialog.selectable.DateFilterTaskHistorySelectableItem
import br.com.sailboat.todozy.core.presentation.dialog.selectable.TaskStatusSelectableItem
import br.com.sailboat.todozy.databinding.DlgFilterTaskHistoryBinding
import br.com.sailboat.todozy.databinding.FrgTaskFormBinding

class TaskHistoryFilterDialog : BaseDialogFragment() {

    var status: TaskStatusSelectableItem? = null
    var date: DateFilterTaskHistorySelectableItem? = null
    private var callback: Callback? = null
    private lateinit var binding: DlgFilterTaskHistoryBinding

    interface Callback {
        fun onClickFilterDate()
        fun onClickFilterStatus()
    }

    companion object {

        fun show(manager: FragmentManager, date: DateFilterTaskHistorySelectableItem,
                 status: TaskStatusSelectableItem, callback: Callback) {

            val dialog = TaskHistoryFilterDialog()
            dialog.callback = callback
            dialog.status = status
            dialog.date = date
            dialog.show(manager, TaskHistoryFilterDialog::class.java.name)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DlgFilterTaskHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
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

    private fun buildDialog() = activity?.run {
        val builder = AlertDialog.Builder(this)
        builder.setView(binding.root)
        builder.create().apply { setView(binding.root) }

    } ?: throw Exception("Context should not be null")

}