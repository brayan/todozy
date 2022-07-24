package br.com.sailboat.uicomponent.impl.dialog.twooptions

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import br.com.sailboat.todozy.utility.android.fragment.BaseDialogFragment

private const val TITLE_KEY = "TITLE_KEY"
private const val MESSAGE_KEY = "MESSAGE_KEY"
private const val POSITIVE_BUTTON_TEXT = "POSITIVE_BUTTON_TEXT"
private const val NEGATIVE_BUTTON_TEXT = "NEGATIVE_BUTTON_TEXT"

class TwoOptionsDialog : BaseDialogFragment() {

    var callback: Callback? = null

    var positiveMsg: Int = android.R.string.ok
    var negativeMsg: Int = android.R.string.cancel

    interface Callback {
        fun onClickPositiveOption()
        fun onClickNegativeOption()
    }

    companion object {
        fun newInstance(
            title: String? = null,
            message: String? = null,
            positiveMsg: Int = android.R.string.ok,
            negativeMsg: Int = android.R.string.cancel,
        ) = TwoOptionsDialog().apply {
            arguments = Bundle().apply {
                putString(TITLE_KEY, title)
                putString(MESSAGE_KEY, message)
                putInt(POSITIVE_BUTTON_TEXT, positiveMsg)
                putInt(NEGATIVE_BUTTON_TEXT, negativeMsg)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        title = arguments?.getString(TITLE_KEY)
        message = arguments?.getString(MESSAGE_KEY)
        positiveMsg = arguments?.getInt(POSITIVE_BUTTON_TEXT) ?: android.R.string.ok
        negativeMsg = arguments?.getInt(NEGATIVE_BUTTON_TEXT) ?: android.R.string.cancel
        return super.onCreateDialog(savedInstanceState)
    }

    override fun bindButtons(alert: AlertDialog.Builder) {
        alert.setPositiveButton(positiveMsg) { _, _ -> callback?.onClickPositiveOption() }
        alert.setNegativeButton(negativeMsg) { _, _ -> callback?.onClickNegativeOption() }
    }
}
