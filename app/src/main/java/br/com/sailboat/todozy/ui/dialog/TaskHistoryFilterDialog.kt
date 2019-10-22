package br.com.sailboat.todozy.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.domain.filter.TaskHistoryFilter
import br.com.sailboat.todozy.ui.base.BaseDialogFragment

class TaskHistoryFilterDialog : BaseDialogFragment() {


    private var filter: TaskHistoryFilter? = null

    private var tvDate: TextView? = null
    private var tvStatus: TextView? = null

    private var callback: Callback? = null

    interface Callback {
        fun onClickFilterDate()
        fun onClickFilterStatus()
    }

    companion object {
        fun show(manager: FragmentManager, filter: TaskHistoryFilter, callback: Callback) {
            val dialog = TaskHistoryFilterDialog()
            dialog.callback = callback
            dialog.setFilter(filter)
            dialog.show(manager, TaskHistoryFilterDialog::class.java.name)
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(activity, R.layout.dlg_filter_task_history, null)
        initViews(view)
        updateViews()

        return construirDialog(view)
    }

    private fun initViews(view: View) {
        tvDate = view.findViewById(R.id.dlg_filter_task_history__tv__date)
        tvStatus = view.findViewById(R.id.dlg_filter_task_history__tv__status)

        view.findViewById<View>(R.id.dlg_filter_task_history__ll__date).setOnClickListener {
            callback!!.onClickFilterDate()
            dialog?.dismiss()
        }

        view.findViewById<View>(R.id.dlg_filter_task_history__ll__status).setOnClickListener {
            callback!!.onClickFilterStatus()
            dialog?.dismiss()
        }
    }

    private fun updateViews() {
        tvDate!!.text = filter?.date?.name
        tvStatus!!.text = filter?.status?.name
    }

    private fun construirDialog(view: View): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        builder.setView(view)

        return builder.create()
    }

    fun getFilter(): TaskHistoryFilter? {
        return filter
    }

    fun setFilter(filter: TaskHistoryFilter) {
        this.filter = filter
    }


}