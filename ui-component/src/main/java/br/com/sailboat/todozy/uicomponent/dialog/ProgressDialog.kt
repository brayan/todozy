package br.com.sailboat.todozy.uicomponent.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import br.com.sailboat.todozy.uicomponent.databinding.DialogProgressBinding
import br.com.sailboat.todozy.utility.android.fragment.BaseDialogFragment

class ProgressDialog : BaseDialogFragment() {

    private lateinit var binding: DialogProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogProgressBinding.inflate(LayoutInflater.from(requireContext()))
        return buildDialog()
    }

    private fun buildDialog(): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)

        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        return dialog
    }
}