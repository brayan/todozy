package br.com.sailboat.uicomponent.impl.dialog

import androidx.appcompat.app.AlertDialog
import br.com.sailboat.todozy.utility.android.fragment.BaseDialogFragment

class TwoOptionsDialog : BaseDialogFragment() {

    private var positiveAndNegativeCallback: PositiveNegativeCallback? = null

    var positiveMsg: Int = android.R.string.ok
    var negativeMsg: Int = android.R.string.cancel
    var positiveCallback: PositiveCallback? = null

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