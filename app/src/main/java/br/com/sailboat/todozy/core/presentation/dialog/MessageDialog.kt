package br.com.sailboat.todozy.core.presentation.dialog

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import br.com.sailboat.todozy.utility.android.fragment.BaseDialogFragment

class MessageDialog : BaseDialogFragment() {

    override fun bindButtons(alert: AlertDialog.Builder) {
        alert.setPositiveButton(android.R.string.ok, null)
    }

    companion object {
        fun showMessage(manager: FragmentManager, message: String, title: String?) {
            val dialog = MessageDialog()
            dialog.message = message
            dialog.title = title
            dialog.show(manager, MessageDialog::class.java.name)
        }
    }
}