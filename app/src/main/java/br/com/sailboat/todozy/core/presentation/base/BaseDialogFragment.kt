package br.com.sailboat.todozy.core.presentation.base

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment : DialogFragment() {

    var message: String? = null
    var title: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alert = AlertDialog.Builder(requireContext())
        bindMessage(alert)
        bindTitle(alert)
        bindButtons(alert)

        return alert.create()
    }

    protected open fun bindButtons(alert: AlertDialog.Builder) {}

    protected open fun bindMessage(alert: AlertDialog.Builder) {
        if (message?.isNotEmpty() == true) {
            alert.setMessage(message)
        }
    }

    protected open fun bindTitle(alert: AlertDialog.Builder) {
        if (title?.isNotEmpty() == true) {
            alert.setTitle(title)
        }
    }
}