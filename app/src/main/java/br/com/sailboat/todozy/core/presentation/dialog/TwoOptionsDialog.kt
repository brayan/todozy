package br.com.sailboat.todozy.core.presentation.dialog

import androidx.appcompat.app.AlertDialog
import br.com.sailboat.todozy.core.presentation.base.BaseDialogFragment

class TwoOptionsDialog : BaseDialogFragment() {

    var positiveMsg: Int = android.R.string.ok
    var negativeMsg: Int = android.R.string.cancel
    var positiveCallback: PositiveCallback? = null
    var positiveAndNegativeCallback: PositiveNegativeCallback? = null

    override fun bindButtons(alert: AlertDialog.Builder) {
        checkAndBindPositiveCallbacks(alert)
        checkAndBindPositiveNegativeCallbacks(alert)
    }

    private fun checkAndBindPositiveCallbacks(alert: AlertDialog.Builder) {
        positiveCallback?.run {
            alert.setPositiveButton(positiveMsg) { _, _ -> positiveCallback!!.onClickPositiveOption() }
            alert.setNegativeButton(negativeMsg, null)
        }
    }

    private fun checkAndBindPositiveNegativeCallbacks(alert: AlertDialog.Builder) {
        positiveAndNegativeCallback?.run {
            alert.setPositiveButton(positiveMsg) { _, _ -> positiveAndNegativeCallback!!.onClickPositiveOption() }
            alert.setNegativeButton(negativeMsg) { _, _ -> positiveAndNegativeCallback!!.onClickNegativeOption() }
        }
    }

    interface PositiveCallback {
        fun onClickPositiveOption()
    }


    interface PositiveNegativeCallback {
        fun onClickPositiveOption()
        fun onClickNegativeOption()
    }
}