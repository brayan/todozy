package br.com.sailboat.uicomponent.impl.helper

import android.content.Context
import androidx.fragment.app.FragmentManager
import br.com.sailboat.uicomponent.impl.R
import br.com.sailboat.uicomponent.impl.dialog.TwoOptionsDialog

class DialogHelper {

    fun showDeleteDialog(
        manager: FragmentManager,
        ctx: Context,
        callback: TwoOptionsDialog.PositiveCallback
    ) {
        val dialog = TwoOptionsDialog()
        dialog.message = ctx.getString(R.string.are_you_sure)
        dialog.positiveMsg = R.string.delete
        dialog.positiveCallback = callback

        dialog.show(manager, "DELETE_DIALOG")
    }

    fun showYesOrNoDialog(
        manager: FragmentManager,
        ctx: Context,
        callback: TwoOptionsDialog.PositiveCallback
    ) {
        val dialog = TwoOptionsDialog()
        dialog.message = ctx.getString(R.string.are_you_sure)
        dialog.positiveMsg = android.R.string.yes
        dialog.negativeMsg = android.R.string.no
        dialog.positiveCallback = callback

        dialog.show(manager, "YES_OR_NO_DIALOG")
    }

}