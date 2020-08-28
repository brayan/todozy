package br.com.sailboat.todozy.core.presentation.dialog

import android.content.Context
import androidx.fragment.app.FragmentManager
import br.com.sailboat.todozy.R

class ErrorDialog {

    companion object {
        fun showDialog(manager: FragmentManager, ctx: Context, e: Exception) {
            val msg = getErrorMessage(ctx, e)
            MessageDialog.showMessage(manager, msg, null)
        }

        private fun getErrorMessage(ctx: Context?, e: Exception?): String {

            return if (e?.message?.isNotEmpty() == true) {
                e.message!!

            } else ctx?.getString(R.string.msg_error)
                    ?: "An error occurred while performing the operation"

        }

    }

}