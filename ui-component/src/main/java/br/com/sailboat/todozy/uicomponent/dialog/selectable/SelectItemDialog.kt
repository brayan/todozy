package br.com.sailboat.todozy.uicomponent.dialog.selectable

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.uicomponent.databinding.DialogRecyclerBinding
import br.com.sailboat.todozy.uicomponent.dialog.selectable.model.SelectableItem
import br.com.sailboat.todozy.utility.android.fragment.BaseDialogFragment
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.visible

class SelectItemDialog(private val callback: Callback) : BaseDialogFragment(),
    SelectableItemAdapter.Callback {

    override lateinit var selectableItems: List<SelectableItem>
    override var selectedItem: SelectableItem? = null

    private lateinit var binding: DialogRecyclerBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogRecyclerBinding.inflate(LayoutInflater.from(requireContext()))

        binding.rvDialogRecycler.layoutManager = LinearLayoutManager(activity)
        binding.rvDialogRecycler.adapter = SelectableItemAdapter(this).apply {
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
            tvDialogRecyclerTitle.text = title
            tvDialogRecyclerTitle.visible()
        } else {
            tvDialogRecyclerTitle.gone()
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