package br.com.sailboat.uicomponent.impl.dialog.selectable

import android.view.ViewGroup
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.visible
import br.com.sailboat.uicomponent.impl.databinding.VhSelectableItemBinding
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.SelectableItem

class SelectableItemViewHolder(parent: ViewGroup, private val callback: Callback) :
    BaseViewHolder<SelectableItem, VhSelectableItemBinding>(
        VhSelectableItemBinding.inflate(getInflater(parent), parent, false)
    ) {

    interface Callback {
        val selectedItem: SelectableItem?
        fun onClickItem(position: Int)
    }

    init {
        itemView.setOnClickListener { callback.onClickItem(bindingAdapterPosition) }
    }

    override fun bind(item: SelectableItem) = with(binding) {
        tvSelectableItemName.setText(item.getName())

        val selectedItem = callback.selectedItem
        if (selectedItem != null && selectedItem === item) {
            ivSelectableItemSelected.visible()
        } else {
            ivSelectableItemSelected.gone()
        }
    }
}
