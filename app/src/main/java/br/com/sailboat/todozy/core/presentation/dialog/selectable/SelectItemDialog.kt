package br.com.sailboat.todozy.core.presentation.dialog.selectable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.core.presentation.helper.gone
import br.com.sailboat.todozy.core.presentation.helper.visible
import br.com.sailboat.todozy.databinding.DlgReyclerBinding

class SelectItemDialog(private val callback: Callback) : DialogFragment(),
    SelectableItemAdapter.Callback {

    override lateinit var selectableItems: List<SelectableItem>
    override var selectedItem: SelectableItem? = null

    private lateinit var binding: DlgReyclerBinding
    var title: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DlgReyclerBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recycler.layoutManager = LinearLayoutManager(context)
        binding.recycler.adapter = SelectableItemAdapter(this).apply {
            submitList(selectableItems)
        }
        updateViews()
    }

    override fun onClickItem(position: Int) {
        callback.onClickItem(selectableItems[position])
        dismiss()
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