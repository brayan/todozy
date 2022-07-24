package br.com.sailboat.uicomponent.impl.helper

import android.content.Context
import androidx.fragment.app.FragmentManager
import br.com.sailboat.uicomponent.impl.R
import br.com.sailboat.uicomponent.impl.dialog.twooptions.TwoOptionsDialog

class DialogHelper {

    fun showDeleteDialog(
        manager: FragmentManager,
        ctx: Context,
        callback: TwoOptionsDialog.Callback
    ) {
        val dialog = TwoOptionsDialog()
        dialog.message = ctx.getString(R.string.are_you_sure)
        dialog.positiveMsg = R.string.delete
        dialog.callback = callback

        dialog.show(manager, "DELETE_DIALOG")
    }
}
