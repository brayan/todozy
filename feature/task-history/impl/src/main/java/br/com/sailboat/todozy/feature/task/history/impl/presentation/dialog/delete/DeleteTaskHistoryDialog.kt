package br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.delete

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import br.com.sailboat.todozy.utility.android.fragment.BaseDialogFragment
import br.com.sailboat.uicomponent.impl.R as UiR

private const val TASK_HISTORY_POSITION = "TASK_HISTORY_POSITION"

class DeleteTaskHistoryDialog : BaseDialogFragment() {
    var callback: Callback? = null
    private var position: Int? = null

    interface Callback {
        fun onConfirmDeleteTaskHistory(position: Int)
    }

    companion object {
        val TAG: String = DeleteTaskHistoryDialog::class.java.name

        fun newInstance(taskHistoryPosition: Int) =
            DeleteTaskHistoryDialog().apply {
                arguments =
                    Bundle().apply {
                        putInt(TASK_HISTORY_POSITION, taskHistoryPosition)
                    }
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        position = arguments?.getInt(TASK_HISTORY_POSITION)
        message = getString(UiR.string.are_you_sure)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun bindButtons(alert: AlertDialog.Builder) {
        alert.setPositiveButton(UiR.string.delete) { _, _ ->
            position?.run { callback?.onConfirmDeleteTaskHistory(this) }
        }
        alert.setNegativeButton(android.R.string.cancel, null)
    }
}
