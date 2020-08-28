package br.com.sailboat.todozy.core.presentation.base

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment : DialogFragment() {

    private var firstSession = true
    var message: String? = null
    var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onResume() {
        super.onResume()

        if (firstSession) {
            onResumeFirstSession()
            firstSession = false
        } else {
            onResumeAfterRestart()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alert = AlertDialog.Builder(activity!!)
        bindMessage(alert)
        bindTitle(alert)
        bindButtons(alert)

        return alert.create()
    }

    protected open fun bindButtons(alert: AlertDialog.Builder) {}

    override fun onDestroyView() {
        if (dialog != null && retainInstance) {
            dialog!!.setDismissMessage(null)
        }
        super.onDestroyView()
    }

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

    protected open fun onResumeFirstSession() {}

    protected open fun onResumeAfterRestart() {}


}