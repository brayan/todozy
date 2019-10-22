package br.com.sailboat.todozy.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.ui.base.BaseDialogFragment

class ProgressDialog: BaseDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(activity, R.layout.dlg_progress, null)
        return buildDialog(view)
    }

    private fun buildDialog(view: View): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        builder.setView(view)

        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        return dialog
    }
}