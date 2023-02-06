package br.com.sailboat.uicomponent.impl.dialog.selectable

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.utility.android.fragment.BaseDialogFragment
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.visible
import br.com.sailboat.uicomponent.impl.databinding.DialogRecyclerBinding
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.SelectableItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectItemDialog :
    BaseDialogFragment(),
    SelectableItemAdapter.Callback {

    override var selectableItems: List<SelectableItem> = emptyList()
    override var selectedItem: SelectableItem? = null
    var callback: Callback? = null

    private lateinit var binding: DialogRecyclerBinding

    private val viewModel: SelectItemViewModel by viewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogRecyclerBinding.inflate(LayoutInflater.from(requireContext()))

        binding.rvDialogRecycler.layoutManager = LinearLayoutManager(activity)
        binding.rvDialogRecycler.adapter = SelectableItemAdapter(this).apply {
            submitList(selectableItems)
        }

        observeViewModel()
        viewModel.dispatchViewIntent(SelectItemViewAction.OnStart(title, selectableItems, selectedItem))

        return buildDialog()
    }

    override fun onClickItem(position: Int) {
        callback?.onClickItem(selectableItems[position])
        dismiss()
    }

    private fun observeViewModel() {
        viewModel.viewState.title.observe(this) { title ->
            this.title = title

            if (title?.isNotEmpty() == true) {
                binding.tvDialogRecyclerTitle.text = title
                binding.tvDialogRecyclerTitle.visible()
            } else {
                binding.tvDialogRecyclerTitle.gone()
            }
        }
        viewModel.viewState.selectableItems.observe(this) { selectableItems ->
            this.selectableItems = selectableItems
        }
        viewModel.viewState.selectedItem.observe(this) { selectedItem ->
            this.selectedItem = selectedItem
        }
    }

    private fun buildDialog(): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        return builder.create()
    }

    interface Callback {
        fun onClickItem(item: SelectableItem)
    }

    companion object {
        fun show(
            tag: String,
            manager: FragmentManager,
            title: String,
            items: List<SelectableItem>,
            selectedItem: SelectableItem?,
            callback: Callback,
        ): SelectItemDialog {
            val dialog = SelectItemDialog()
            dialog.callback = callback
            dialog.selectableItems = items
            dialog.selectedItem = selectedItem
            dialog.title = title
            dialog.show(manager, tag)

            return dialog
        }
    }
}
