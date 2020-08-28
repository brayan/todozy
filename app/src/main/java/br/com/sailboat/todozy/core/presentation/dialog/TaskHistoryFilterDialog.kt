package br.com.sailboat.todozy.core.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.presentation.base.BaseDialogFragment
import br.com.sailboat.todozy.core.presentation.dialog.selectable.DateFilterTaskHistorySelectableItem
import br.com.sailboat.todozy.core.presentation.dialog.selectable.TaskStatusSelectableItem
import kotlinx.android.synthetic.main.dlg_filter_task_history.view.*

class TaskHistoryFilterDialog : BaseDialogFragment() {

    var status: TaskStatusSelectableItem? = null
    var date: DateFilterTaskHistorySelectableItem? = null
    private var callback: Callback? = null
    private lateinit var customView: View

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


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        customView = View.inflate(activity, R.layout.dlg_filter_task_history, null)
        initViews()
        updateViews()
        return buildDialog()
    }

    private fun initViews() = with(customView) {
        tvDate.setOnClickListener {
            callback?.onClickFilterDate()
            dialog?.dismiss()
        }

        tvStatus.setOnClickListener {
            callback?.onClickFilterStatus()
            dialog?.dismiss()
        }
    }

    private fun updateViews() = with(customView) {
        date?.run { tvDate.setText(getName()) }
        status?.run { tvStatus.setText(getName()) }
    }

    private fun buildDialog() = activity?.run {
        val builder = AlertDialog.Builder(this)
        builder.setView(customView)
        builder.create().apply { setView(customView) }

    } ?: throw Exception("Context should not be null")

}