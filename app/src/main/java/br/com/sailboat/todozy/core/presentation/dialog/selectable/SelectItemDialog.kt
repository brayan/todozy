package br.com.sailboat.todozy.core.presentation.dialog.selectable

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.core.presentation.base.BaseDialogFragment
import br.com.sailboat.todozy.core.presentation.helper.gone
import br.com.sailboat.todozy.core.presentation.helper.visible
import br.com.sailboat.todozy.databinding.DlgReyclerBinding

class SelectItemDialog(private val callback: Callback) : BaseDialogFragment(),
    SelectableItemAdapter.Callback {

    override lateinit var selectableItems: List<SelectableItem>
    override var selectedItem: SelectableItem? = null

    private lateinit var binding: DlgReyclerBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DlgReyclerBinding.inflate(LayoutInflater.from(requireContext()))

        binding.recycler.layoutManager = LinearLayoutManager(activity)
        binding.recycler.adapter = SelectableItemAdapter(this).apply {
            submitList(selectableItems)
        }
        updateViews()

        return buildDialog()
    }

    override fun onClickItem(position: Int) {
        callback.onClickItem(selectableItems[position])
        dismiss()
    }

    private fun buildDialog(): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        return builder.create()
    }

    private fun updateViews() = with(binding) {
        if (title?.isNotEmpty() == true) {
            dlgRecyclerTvTitle.text = title
            dlgRecyclerTvTitle.visible()
        } else {
            dlgRecyclerTvTitle.gone()
        }
    }

    interface Callback {
        fun onClickItem(item: SelectableItem)
    }

    companion object {
        fun show(
            manager: FragmentManager,
            title: String,
            items: List<SelectableItem>,
            selectedItem: SelectableItem?,
            callback: Callback,
        ) {
            val dialog = SelectItemDialog(callback)
            dialog.selectableItems = items
            dialog.selectedItem = selectedItem
            dialog.title = title
            dialog.show(manager, SelectItemDialog::class.java.name)
        }
    }
}